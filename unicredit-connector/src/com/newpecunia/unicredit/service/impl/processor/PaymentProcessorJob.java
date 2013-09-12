package com.newpecunia.unicredit.service.impl.processor;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.google.inject.Inject;
import com.newpecunia.configuration.NPConfiguration;
import com.newpecunia.time.TimeProvider;
import com.newpecunia.unicredit.service.BalanceService;
import com.newpecunia.unicredit.service.ForeignPayment;
import com.newpecunia.unicredit.service.impl.entity.ForeignPaymentMapper;
import com.newpecunia.unicredit.service.impl.entity.ForeignPaymentOrder;
import com.newpecunia.unicredit.webdav.UnicreditWebdavService;
import com.newpecunia.unicredit.webdav.impl.MulticashForeignPaymentPackage;

public class PaymentProcessorJob implements Runnable {

	private static final Logger logger = LogManager.getLogger(PaymentProcessorJob.class);	
	
	private UnicreditWebdavService webdavService;
	private ForeignPaymentMapper foreignPaymentMapper;
	private BalanceService balanceService;
	private TimeProvider timeProvider;
	private NPConfiguration configuration;

	private Session session; //TODO
	
	@Inject
	public PaymentProcessorJob(UnicreditWebdavService webdavService, BalanceService balanceService, ForeignPaymentMapper foreignPaymentMapper, TimeProvider timeProvider, NPConfiguration configuration) {
		this.webdavService = webdavService;
		this.balanceService = balanceService;
		this.foreignPaymentMapper = foreignPaymentMapper;
		this.timeProvider = timeProvider;
		this.configuration = configuration;
	}
	
	@Override
	public void run() {
		processPendingPayments();
	}

	private void processPendingPayments() {
		logger.info("Sending pending payments to webdav.");
		
		Criteria crit = session.createCriteria(ForeignPaymentOrder.class)
			.add(Restrictions.eqOrIsNull("status", ForeignPaymentOrder.PaymentStatus.NEW))
			.addOrder(Order.asc("createTimestamp"));
		
		@SuppressWarnings("unchecked")		
		List<ForeignPaymentOrder> newPayments = crit.list();
		List<String> unprocessedPaymentIds = new ArrayList<>();		
		for (ForeignPaymentOrder foreignPaymentOrder : newPayments) {
			String paymentId = foreignPaymentOrder.getId();
			logger.info("Processing payment "+paymentId+" for "+foreignPaymentOrder.getName());
			ForeignPayment foreignPayment = foreignPaymentMapper.mapToPayment(foreignPaymentOrder);

			BigDecimal balance = balanceService.getApproximateBalance();
			BigDecimal fee = configuration.getPaymentFee();
			BigDecimal reserve = configuration.getUnicreditReserve();
			BigDecimal balanceAfterPayment = balance.subtract(foreignPayment.getAmount()).subtract(fee);
			
			if (balanceAfterPayment.compareTo(reserve) < 0) { //not enough money on the account
				logger.info("Not enough money on bank account to process payment "+paymentId+" for "+foreignPayment.getName()
						+". Payment amount: "+foreignPayment.getAmount().toPlainString()+". Account balance: "+balance.toPlainString()
						+". Account reserve: "+reserve.toPlainString()+". Payment fee: "+fee.toPlainString());
				unprocessedPaymentIds.add(paymentId);
				continue;
			}

			try {
				webdavService.uploadForeignPaymentPackage(paymentId, foreignPayment);
			} catch (IOException e) {
				unprocessedPaymentIds.add(paymentId);
				String message = "Webdav is out of order now. Processing of payments stopped for this run."; 
				logger.info(message);
				logger.info(message, e);
				break;
			}
						
			balanceService.substractFromBalance(foreignPayment.getAmount(), foreignPayment.getCurrency());
			balanceService.substractFromBalance(fee, foreignPayment.getCurrency());
		}
		logger.info("Finished sending pending payments to webdav. Unprocessed payments: "+unprocessedPaymentIds);
	}
	

}
