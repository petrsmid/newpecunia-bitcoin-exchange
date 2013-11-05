package com.newpecunia.unicredit.service.impl.processor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.newpecunia.NPException;
import com.newpecunia.configuration.NPConfiguration;
import com.newpecunia.persistence.entities.ForeignPaymentOrder;
import com.newpecunia.persistence.entities.ForeignPaymentOrder.PaymentStatus;
import com.newpecunia.time.TimeProvider;

@DisallowConcurrentExecution
public class PreorderCleanerJob implements Job {

	private static final Logger logger = LogManager.getLogger(PreorderCleanerJob.class);	
	
	private NPConfiguration configuration;
	private TimeProvider timeProvider;
	private Provider<EntityManager> enitityManagerProvider;
	
	
	
	@Inject
	public PreorderCleanerJob(Provider<EntityManager> enitityManagerProvider, 
			NPConfiguration configuration, TimeProvider timeProvider) {
		this.configuration = configuration;
		this.timeProvider = timeProvider;
		this.enitityManagerProvider = enitityManagerProvider;
	}
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("Starting job "+PreorderCleanerJob.class.getSimpleName());
		cleanPreorders();
		logger.info("Finished job "+PreorderCleanerJob.class.getSimpleName());
	}

	private void cleanPreorders() {
		logger.info("Cleaning old orders waiting for BTC. Setting to TIMEOUTED.");
		EntityManager entityManager = enitityManagerProvider.get();
		entityManager.clear();
		Session session = entityManager.unwrap(Session.class);
		
		Calendar now = timeProvider.nowCalendar();
		Calendar oldestTimestamp = timeProvider.nowCalendar();
		oldestTimestamp.add(Calendar.SECOND, -configuration.getMaxTimeToWaitForBtcInSeconds()); //move into history

		timeoutOldPreorders(session, oldestTimestamp, now);
		List<ForeignPaymentOrder> timeoutedPreorders = loadTheTimeoutedPreOrders(session, now);
		List<String> timeoutedOrderIds = new ArrayList<>();
		for (ForeignPaymentOrder order : timeoutedPreorders) {
			timeoutedOrderIds.add(order.getId());
			logger.debug(String.format("Setting preorder to timeouted. Id: %s E-mail: %s", order.getId(), order.getRequestorEmail()));
		}
		logger.info("Setting preorders to timeouted: "+timeoutedOrderIds);
		entityManager.clear();
	}


	@SuppressWarnings("unchecked")
	private List<ForeignPaymentOrder> loadTheTimeoutedPreOrders(Session session, Calendar now) {
		return session.createCriteria(ForeignPaymentOrder.class)
				.add(Restrictions.eq("status", PaymentStatus.TIMEOUTED_WAITING_FOR_BTC))
				.add(Restrictions.eq("updateTimestamp", now))
				.list();
	}

	private void timeoutOldPreorders(Session session, Calendar oldestTimestamp, Calendar now) {
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Query query = session.createQuery("update "+ForeignPaymentOrder.class.getName()+" " +
					"set status = :timeoutedStatus, updateTimestamp = :updateTimestamp where status = :waitingForBtcStatus and updateTimestamp < :oldestTimestamp");
			query.setParameter("timeoutedStatus", PaymentStatus.TIMEOUTED_WAITING_FOR_BTC);
			query.setParameter("updateTimestamp", now);
			query.setParameter("waitingForBtcStatus", PaymentStatus.WAITING_FOR_BTC);
			query.setParameter("oldestTimestamp", oldestTimestamp);
			int rowsChanged = query.executeUpdate();
			logger.info("Number of preorders which timeouted: "+rowsChanged);
			session.flush();
			tx.commit();
		} catch (Exception e) {
			try {
				tx.rollback();
			} catch (Exception rollbackException) {
				//do nothing - the NPException is following.
			}
			throw new NPException("Error ocurred while setting old preorders to timeouted .", e);
		}
	}
	
	/**
	 * Only for testing purposes.
	 */
	void _testCleanPreorders() {
		cleanPreorders();
	}

}
