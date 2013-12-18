package com.newpecunia.trader.service.impl.processor;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.newpecunia.bitcoind.service.BitcoindService;
import com.newpecunia.bitstamp.service.BitstampServiceException;
import com.newpecunia.configuration.NPConfiguration;
import com.newpecunia.persistence.entities.BtcPaymentOrder;
import com.newpecunia.persistence.entities.BtcPaymentOrder.BtcOrderStatus;
import com.newpecunia.time.TimeProvider;

@DisallowConcurrentExecution
public class BtcOrderProcessorAndSettleUpJob implements Job {
	
	private static final Logger logger = LogManager.getLogger(BtcOrderProcessorAndSettleUpJob.class);	
	
	private BitcoindService bitcoindService;
	private Provider<EntityManager> emProvider;
	private TimeProvider timeProvider;
	private NPConfiguration configuration;
	private BitstampWithdrawOrderManager bitstampWithdrawManager;
	
	@Inject
	BtcOrderProcessorAndSettleUpJob(Provider<EntityManager> emProvider, 
			TimeProvider timeProvider, 
			BitcoindService bitcoindService,
			BitstampWithdrawOrderManager bitstampWithdrawManager,
			NPConfiguration configuration) {
		this.emProvider = emProvider;
		this.timeProvider = timeProvider;
		this.bitcoindService = bitcoindService;
		this.bitstampWithdrawManager = bitstampWithdrawManager;
		this.configuration = configuration;
	}
	
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("Starting BTC settle up job.");
		BigDecimal neededAdditionalAmount = processPendingBtcOrders();
		
		settleUpTheBalances(neededAdditionalAmount);
		logger.info("BTC settle up job finished.");
	}


	private void settleUpTheBalances(BigDecimal neededAdditionalAmount) {
		try {
			BigDecimal actualBalance = bitcoindService.getBalance();
			if (actualBalance.compareTo(configuration.getOptimalBtcWalletBalance()) < 0) {
				transferFromBitstampToWallet(configuration.getOptimalBtcWalletBalance().add(neededAdditionalAmount).subtract(actualBalance));
			} else if ((neededAdditionalAmount.compareTo(BigDecimal.ZERO)==0) &&
					(actualBalance.compareTo(configuration.getOptimalBtcWalletBalance()) > 0)) {
				transferToBitstamp(actualBalance.subtract(configuration.getOptimalBtcWalletBalance()));
			}
		} catch (BitstampServiceException e) {
			logger.error("Unable to settle up the balances. Problems with Bitstamp.", e);
		}
	}


	private BigDecimal processPendingBtcOrders() {
		Session session = emProvider.get().unwrap(Session.class);
		session.clear();		
		@SuppressWarnings("unchecked")
		List<BtcPaymentOrder> unprocessedOrders = session.createCriteria(BtcPaymentOrder.class)
			.add(Restrictions.eq("status", BtcOrderStatus.UNPROCESSED))
			.add(Restrictions.eq("askedForBtcOnStock", false))
			.addOrder(Order.asc("createTimestamp"))		
			.list();
		

		EntityTransaction tx = emProvider.get().getTransaction();
		tx.begin();
		
		BigDecimal stillUnprocessedAmount = BigDecimal.ZERO;
		try {
			for (BtcPaymentOrder order : unprocessedOrders) {
				order.setAskedForBtcOnStock(true);
				BigDecimal balance = bitcoindService.getBalance();
				if (balance.compareTo(order.getAmount().add(configuration.getBitcoindTransactionFee())) >= 0) {
					try {
						logger.info("Sending BTC to customer: "+order.getAmount().toPlainString()+" BTC to address "+order.getAddress());
						bitcoindService.sendMoney(order.getAddress(), order.getAmount(), "To customer", "New Pecunia");
						order.setStatus(BtcOrderStatus.PROCESSED);
						order.setUpdateTimestamp(timeProvider.nowCalendar());
						session.update(order);
						session.flush();
					} catch (Exception e) {
						logger.error("Error ocurred while processing BTC order.", e);
						break;
					}
				} else {
					logger.info("Not enough BTC in wallet to send "+order.getAmount().toPlainString()
							+" BTC to address "+order.getAddress()+". Waiting until corresponding amount will be bought on Bitstamp.");
					checkStarvingOrder(order);
					stillUnprocessedAmount = stillUnprocessedAmount.add(order.getAmount());
				}
			}
		} finally {
			tx.commit();
		}
		session.clear();
		return stillUnprocessedAmount;
	}
	
	
	

	private void checkStarvingOrder(BtcPaymentOrder order) {
		long now = timeProvider.now();
		long orderCreateTime = order.getCreateTimestamp().getTimeInMillis();
		if ((now - orderCreateTime) > (configuration.getAgeOfBtcOrderToReportInSec()*1000)) {
			logger.error("BTC Order "+order.getId()+" is too old and starving!");
		}
		
	}


	private void transferToBitstamp(BigDecimal amount) throws BitstampServiceException {
		logger.trace("Transferring BTC from wallet to Bitstamp.");
		if (amount.compareTo(configuration.getBitstampMinimalBtcOrder()) < 0) {
			return; //do nothing
		}

		bitcoindService.sendMoney(configuration.getBitstampBtcAddress(), amount, "To Bitstamp", "");
	}


	private void transferFromBitstampToWallet(BigDecimal amount) throws BitstampServiceException {
		logger.trace("Transferring BTC from Bitstamp to wallet.");
		if (amount.compareTo(configuration.getBitstampMinimalBtcOrder()) < 0) {
			amount = configuration.getBitstampMinimalBtcOrder();
		}
		
		bitstampWithdrawManager.orderWithdrawBtcFromBitstampToWallet(amount);
	}



}
