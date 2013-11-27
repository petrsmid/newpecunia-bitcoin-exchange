package com.newpecunia.unicredit.service.impl.processor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.newpecunia.persistence.entities.ForeignPaymentOrder;
import com.newpecunia.persistence.entities.ForeignPaymentOrder.PaymentStatus;
import com.newpecunia.time.TimeProvider;
import com.newpecunia.unicredit.service.ForeignPayment.PayeeType;
import com.newpecunia.unicredit.webdav.UnicreditWebdavService;

@DisallowConcurrentExecution
public class PaymentStatementStatusUpdaterJob implements Job {

	private static final Logger logger = LogManager.getLogger(PaymentStatementStatusUpdaterJob.class);	
	
	private UnicreditWebdavService webdavService;
	private Provider<EntityManager> enitityManagerProvider;
	private TimeProvider timeProvider;

	@Inject
	public PaymentStatementStatusUpdaterJob(Provider<EntityManager> enitityManagerProvider, UnicreditWebdavService webdavService, TimeProvider timeProvider) {
		this.enitityManagerProvider = enitityManagerProvider;
		this.webdavService = webdavService;
		this.timeProvider = timeProvider;
	}
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("Starting job "+PaymentStatementStatusUpdaterJob.class.getSimpleName());
		Session session = enitityManagerProvider.get().unwrap(Session.class);
		session.clear();
		Transaction tx = session.beginTransaction();
		
		updateStatusOfPaymentsAccordingToStatus(session);
		
		session.flush();
		tx.commit();
		session.clear();
		logger.info("Finished job "+PaymentStatementStatusUpdaterJob.class.getSimpleName());
	}

	private void updateStatusOfPaymentsAccordingToStatus(Session session) {
		List<ForeignPaymentOrder> payments = loadPaymentsSentToWebdav(session);
		List<String> outgoingRefs;
		int paymentsToBitstampCount = 0;
		try {
			outgoingRefs = webdavService.findOutgoingNonBitstampPaymentRefsInLastStatement();
			paymentsToBitstampCount = webdavService.getOutgoingPaymentsToBitstampCount();
		} catch (IOException e) {
			logger.error("Cannot connect to webdav.", e);
			return;
		}

		//set status of payments with found reference to PROCESSED
		for (ForeignPaymentOrder payment : payments) {
			String ref = payment.getShortUnicreditReference();
			if (outgoingRefs.contains(ref)) {
				payment.setStatus(PaymentStatus.PROCESSED);
				payment.setUpdateTimestamp(timeProvider.nowCalendar());
			}
		}
		
		//set status of first N payments to Bitstamp to PROCESSED (we do not know the reference but we know the order of them in time)
		List<ForeignPaymentOrder> paymentsToBitstamp = getPaymentsToBitstamp(payments);
		for (int i = 0; i < paymentsToBitstamp.size(); i++) {
			if (i < paymentsToBitstampCount) {
				ForeignPaymentOrder paymentToBitstamp = paymentsToBitstamp.get(i);
				paymentToBitstamp.setStatus(PaymentStatus.PROCESSED);
				paymentToBitstamp.setUpdateTimestamp(timeProvider.nowCalendar());
			}
		}
		
		
	}
	

	private List<ForeignPaymentOrder> getPaymentsToBitstamp(List<ForeignPaymentOrder> payments) {
		List<ForeignPaymentOrder> paymentsToBitstamp = new ArrayList<>();
		for (ForeignPaymentOrder payment : payments) {
			if (PayeeType.BITSTAMP.equals(payment.getPayeeType())) {
				paymentsToBitstamp.add(payment);
			}
		}
		return paymentsToBitstamp;
	}

	@SuppressWarnings("unchecked")
	private List<ForeignPaymentOrder> loadPaymentsSentToWebdav(Session session) {
		return session.createCriteria(ForeignPaymentOrder.class)
			.add(Restrictions.in("status", new PaymentStatus[] {
					PaymentStatus.SENT_TO_WEBDAV,
					PaymentStatus.WEBDAV_PENDING,
					PaymentStatus.WEBDAV_SIGNED
				}))
			.addOrder(Order.asc("updateTimestamp"))
			.list();
	}	

}
