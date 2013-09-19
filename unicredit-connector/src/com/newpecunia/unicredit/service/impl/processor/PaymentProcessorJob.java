package com.newpecunia.unicredit.service.impl.processor;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.google.inject.Inject;
import com.newpecunia.NPException;
import com.newpecunia.configuration.NPConfiguration;
import com.newpecunia.time.TimeProvider;
import com.newpecunia.unicredit.service.BalanceService;
import com.newpecunia.unicredit.service.ForeignPayment;
import com.newpecunia.unicredit.service.impl.entity.ForeignPaymentMapper;
import com.newpecunia.unicredit.service.impl.entity.ForeignPaymentOrder;
import com.newpecunia.unicredit.service.impl.entity.ForeignPaymentOrder.PaymentStatus;
import com.newpecunia.unicredit.webdav.UnicreditWebdavService;

public class PaymentProcessorJob implements Runnable {

	private static final Logger logger = LogManager.getLogger(PaymentProcessorJob.class);	
	
	private SessionFactory sessionFactory;
	private UnicreditWebdavService webdavService;
	private ForeignPaymentMapper foreignPaymentMapper;
	private BalanceService balanceService;
	private NPConfiguration configuration;
	private TimeProvider timeProvider;

	
	@Inject
	public PaymentProcessorJob(SessionFactory sessionFactory, UnicreditWebdavService webdavService, BalanceService balanceService, 
			ForeignPaymentMapper foreignPaymentMapper, NPConfiguration configuration, TimeProvider timeProvider) {
		this.webdavService = webdavService;
		this.balanceService = balanceService;
		this.foreignPaymentMapper = foreignPaymentMapper;
		this.configuration = configuration;
		this.timeProvider = timeProvider;
	}
	
	@Override
	public void run() {
		processPendingPayments();
	}

	private void processPendingPayments() {
		logger.info("Sending pending payments to webdav.");
		
		Session session = sessionFactory.openSession();
		//load new payments
		List<ForeignPaymentOrder> newPayments = loadNewPaymentsFromDB(session);
		
		List<String> sentPaymentIds = new ArrayList<>();		
		List<String> unprocessedPaymentIds = new ArrayList<>();		
		for (ForeignPaymentOrder foreignPaymentOrder : newPayments) {
			String paymentId = foreignPaymentOrder.getId();
			logger.info("Processing payment "+paymentId+" for "+foreignPaymentOrder.getName());
			ForeignPayment foreignPayment = foreignPaymentMapper.mapToPayment(foreignPaymentOrder);

			BigDecimal balance = balanceService.getApproximateBalance();
			BigDecimal fee = configuration.getPaymentFee();
			BigDecimal reserve = configuration.getUnicreditReserve();
			BigDecimal balanceAfterPayment = balance.subtract(foreignPayment.getAmount()).subtract(fee);
			
			//check available balance
			if (balanceAfterPayment.compareTo(reserve) < 0) { //not enough money on the account
				logger.info("Not enough money on bank account to process payment "+paymentId+" for "+foreignPayment.getName()
						+". Payment amount: "+foreignPayment.getAmount().toPlainString()+". Account balance: "+balance.toPlainString()
						+". Account reserve: "+reserve.toPlainString()+". Payment fee: "+fee.toPlainString());
				unprocessedPaymentIds.add(paymentId);
				continue;
			}

			//send to webdav
			try {
				webdavService.uploadForeignPaymentPackage(paymentId, foreignPayment);
			} catch (IOException e) {
				unprocessedPaymentIds.add(paymentId);
				String message = "Webdav is out of order now. Processing of payments stopped for this run."; 
				logger.info(message);
				logger.info(message, e);
				break;
			}

			sentPaymentIds.add(paymentId);
			
			//adjust available balance
			balanceService.substractFromBalance(foreignPayment.getAmount(), foreignPayment.getCurrency());
			balanceService.substractFromBalance(fee, foreignPayment.getCurrency());
			
			//update status of the payment
			updateStatusOfPaymentInDB(session, foreignPaymentOrder);
		}

		session.close();
		
		logger.info("Finished sending pending payments to webdav. " +
				"Payments sent to webdav: "+sentPaymentIds+". " +
				"Unprocessed payments: "+unprocessedPaymentIds);
	}

	private List<ForeignPaymentOrder> loadNewPaymentsFromDB(Session session) {
		Criteria crit = session.createCriteria(ForeignPaymentOrder.class)
			.add(Restrictions.eqOrIsNull("status", ForeignPaymentOrder.PaymentStatus.NEW))
			.addOrder(Order.asc("createTimestamp"));		
		@SuppressWarnings("unchecked")		
		List<ForeignPaymentOrder> newPayments = crit.list();
		return newPayments;
	}

	private void updateStatusOfPaymentInDB(Session session, ForeignPaymentOrder foreignPaymentOrder) {
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			foreignPaymentOrder.setStatus(PaymentStatus.SENT_TO_WEBDAV);
			foreignPaymentOrder.setUpdateTimestamp(timeProvider.nowCalendar());
			session.update(foreignPaymentOrder);
			session.flush();
			tx.commit();
		} catch (Exception e) {
			try {
				tx.rollback();
			} catch (Exception rollbackException) {
				//do nothing - the NPException is following.
			}
			throw new NPException("Error ocurred while setting status of payment "+ foreignPaymentOrder.getId() + 
					" to "+PaymentStatus.SENT_TO_WEBDAV+". Payee: "+foreignPaymentOrder.getName(), e);
		}
	}
	

}
