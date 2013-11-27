package com.newpecunia.trader.service.impl.processor;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.newpecunia.bitstamp.webdriver.BitstampWebdriver;
import com.newpecunia.bitstamp.webdriver.BitstampWebdriverException;
import com.newpecunia.configuration.NPConfiguration;
import com.newpecunia.countries.CountryDatabase;
import com.newpecunia.persistence.entities.ForeignPaymentOrder;
import com.newpecunia.persistence.entities.ForeignPaymentOrder.PaymentStatus;
import com.newpecunia.time.SKBusinessDayPlanner;
import com.newpecunia.time.TimeProvider;
import com.newpecunia.unicredit.service.BalanceService;
import com.newpecunia.unicredit.service.ForeignPayment;
import com.newpecunia.unicredit.service.ForeignPayment.PayeeType;
import com.newpecunia.unicredit.service.PaymentService;

@DisallowConcurrentExecution
public class MoneySettleUpJob implements Job {

	
	private static final Logger logger = LogManager.getLogger(MoneySettleUpJob.class);	
	
	private BalanceService balanceService;
	private PaymentService paymentService;
	private BitstampWebdriver bitstampWebdriver;
	private BitstampAutoTrader bitstampAutoTrader;
	private TimeProvider timeProvider;
	private SKBusinessDayPlanner businessDayPlanner;
	private Provider<EntityManager> emProvider;
	private NPConfiguration configuration;
	private CountryDatabase countryDb;

	
	@Inject
	MoneySettleUpJob(Provider<EntityManager> emProvider,
			BalanceService balanceService,
			PaymentService paymentService,
			BitstampWebdriver bitstampWebdriver,
			BitstampAutoTrader bitstampAutoTrader, 
			TimeProvider timeProvider,
			SKBusinessDayPlanner businessDayPlanner,
			CountryDatabase countryDb,			
			NPConfiguration configuration) {
		
		this.paymentService = paymentService;
		this.balanceService = balanceService;
		this.bitstampWebdriver = bitstampWebdriver;
		this.configuration = configuration;
		this.timeProvider = timeProvider;
		this.businessDayPlanner = businessDayPlanner;
		this.emProvider = emProvider;
		this.bitstampAutoTrader = bitstampAutoTrader;
		this.countryDb = countryDb;
	}	
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("Starting job "+MoneySettleUpJob.class.getSimpleName());
		Session session = emProvider.get().unwrap(Session.class);
		session.clear();
				
		settleUpMoney(session);

		session.flush();
		session.clear();
		logger.info("Finished job "+MoneySettleUpJob.class.getSimpleName());
	}

	private void settleUpMoney(Session session) {
		List<ForeignPaymentOrder> oldPendingPayments = loadOldPendingPayments(session);
		BigDecimal sumOldPending = sumAmount(oldPendingPayments);
		BigDecimal actualBalance = balanceService.getApproximateBalance();
		
		if ((sumOldPending.compareTo(BigDecimal.ZERO) > 0)
				&& (sumOldPending.compareTo(actualBalance) < 0)) {
			logger.error(String.format("Something went wrong - the account balance is %s USD but the orders for %s USD were not processed.",
					actualBalance.toPlainString(), sumOldPending.toPlainString()));
			return;
		}
		
		if (sumOldPending.compareTo(BigDecimal.ZERO) > 0) {
			transferMoneyFromBitstamp(sumOldPending);
			setAlreadyAskedForMoneyFlag(session, oldPendingPayments);
		} else {
			//send the rest balance to the Bitstamp
			try {
				if (bitstampWebdriver.isWaitingForDeposit()) {
					logger.info("Cannot send USDs to Bitstamp now - it is in status waiting for deposit now.");
				} else {
					logger.info("Sending USDs to Bitstamp.");
					BigDecimal reserve = configuration.getUnicreditReserve();
					BigDecimal amountToSend = actualBalance.subtract(reserve);
					BigDecimal roundedAmount = amountToSend.setScale(0, RoundingMode.HALF_UP);
					
					bitstampWebdriver.createInternationalUSDDeposit(roundedAmount.intValue(),
							configuration.getBitstampDepositName(), configuration.getBitstampDepositSurname(), "");
					
					ForeignPayment payment = createPaymentToBitstamp(roundedAmount);
					paymentService.createForeignPaymentOrder(payment);
					
				}
			} catch (IOException | BitstampWebdriverException e) {
				logger.error("Error ocurred while trying to deposit USD to Bitstamp.", e);
				return;
			}
		}
	}

	private ForeignPayment createPaymentToBitstamp(BigDecimal amount) {
		ForeignPayment payment = new ForeignPayment();
		payment.setAmount(amount);
		payment.setCurrency(configuration.getBitstampDepositAccountCurrency());
		payment.setPayeeType(PayeeType.BITSTAMP);
		payment.setName(configuration.getBitstampDepositAccountName());
		payment.setAddress(configuration.getBitstampDepositAddress());
		payment.setCity(configuration.getBitstampDepositCity());
		payment.setPostalCode(configuration.getBitstampDepositPostalCode());
		payment.setCountry(countryDb.getCountryForISO(configuration.getBitstampDepositCountry()));
		payment.setRequestorEmail(configuration.getGmailAddress());
		payment.setAccountNumber(configuration.getBitstampDepositBankIban());
		payment.setSwift(configuration.getBitstampDepositBankSwift());
		payment.setBankName(configuration.getBitstampDepositBankName());
		payment.setBankAddress(configuration.getBitstampDepositBankAddress());
		payment.setBankCity(configuration.getBitstampDepositBankCity());
		payment.setBankPostalCode(configuration.getBitstampDepositBankPostalCode());
		payment.setBankCountry(countryDb.getCountryForISO(configuration.getBitstampDepositBankCountry()));
		return payment;
	}

	private void setAlreadyAskedForMoneyFlag(Session session, List<ForeignPaymentOrder> oldPendingPayments) {
		Transaction tx = session.getTransaction();
		tx.begin();
		
		for (ForeignPaymentOrder order : oldPendingPayments) {
			order.setAskedForMoneyOnStock(true);
			session.update(order);
		}
		
		tx.commit();
	}

	private void transferMoneyFromBitstamp(BigDecimal amountUSD) {
		if (amountUSD.compareTo(configuration.getBitstampMinimalUsdWithdraw()) < 0) {
			amountUSD = configuration.getBitstampMinimalUsdWithdraw();
		}
		bitstampAutoTrader.sendUsdToUnicredit(amountUSD);
	}

	private BigDecimal sumAmount(List<ForeignPaymentOrder> oldPendingPayments) {
		BigDecimal sum = BigDecimal.ZERO;
		for (ForeignPaymentOrder order : oldPendingPayments) {
			sum.add(order.getAmount());
		}
		return sum;
	}

	@SuppressWarnings("unchecked")
	private List<ForeignPaymentOrder> loadOldPendingPayments(Session session) {
		Calendar now = timeProvider.nowCalendar();
		Calendar midnightYeasterday = businessDayPlanner.moveByBusinessDays(now, -1);
		
		return session.createCriteria(ForeignPaymentOrder.class)
			.add(Restrictions.eq("status", PaymentStatus.NEW))
			.add(Restrictions.eq("askedForMoneyOnStock", false))
			.add(Restrictions.le("createTimestamp", midnightYeasterday))
			.list();
	}

}
