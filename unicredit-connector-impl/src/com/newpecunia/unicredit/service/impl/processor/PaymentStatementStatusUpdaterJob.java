package com.newpecunia.unicredit.service.impl.processor;

import java.io.IOException;
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
		Transaction tx = session.beginTransaction();
		
		updateStatusOfPaymentsAccordingToStatus(session);
		
		session.flush();
		tx.commit();
		enitityManagerProvider.get().close();
		logger.info("Finished job "+PaymentStatementStatusUpdaterJob.class.getSimpleName());
	}

	private void updateStatusOfPaymentsAccordingToStatus(Session session) {
		List<ForeignPaymentOrder> payments = loadPaymentsSentToWebdav(session);
		List<String> outgoingRefs;
		try {
			outgoingRefs = webdavService.findOutgoingPaymentRefsInLastStatement();
		} catch (IOException e) {
			logger.error("Cannot connect to webdav.", e);
			return;
		}
		
		for (ForeignPaymentOrder payment : payments) {
			String ref = payment.getShortUnicreditReference();
			if (outgoingRefs.contains(ref)) {
				payment.setStatus(PaymentStatus.PROCESSED);
				payment.setUpdateTimestamp(timeProvider.nowCalendar());
			}
		}
		
		
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
