package com.newpecunia.unicredit.service.impl.processor;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.newpecunia.configuration.NPConfiguration;
import com.newpecunia.persistence.entities.ForeignPaymentOrder;
import com.newpecunia.persistence.entities.ForeignPaymentOrder.PaymentStatus;
import com.newpecunia.time.TimeProviderMock;


public class PreorderCleanerJobTest {


	private TimeProviderMock timeProviderMock = new TimeProviderMock();
	private PreorderCleanerJob preorderCleanerJob;
	private PersistService persistence = null;
	private Provider<EntityManager> emProvider;
	private Calendar oldDate;
	
	@Before
	public void setup() {
		Calendar now = new GregorianCalendar();
		oldDate = new GregorianCalendar();
		oldDate.add(Calendar.DATE, -2); //two days before now		
		timeProviderMock.setCalendar(now);
		
		Injector injector = Guice.createInjector(new JpaPersistModule("testingJpaUnit"));
		persistence = injector.getInstance(PersistService.class);
		emProvider = injector.getProvider(EntityManager.class);
		persistence.start();
		this.preorderCleanerJob = new PreorderCleanerJob(emProvider, new NPConfiguration() {
			@Override
			public void reloadConfiguration() {
				//do nothing
			}
			@Override
			public int getMaxTimeToWaitForBtcInSeconds() {
				return 3600*24; //one day
			}
		}, timeProviderMock);
		
		
		cleanPaymentsTable();
		createTwoPreOrders();
	}
	
	@After
	public void tearDown() {
		cleanPaymentsTable();
		persistence.stop();
	}

	@Test
	public void testCleanerJob() {
		preorderCleanerJob._testCleanPreorders();
		List<ForeignPaymentOrder> orders = getAllOrders();
		Assert.assertEquals(2, orders.size());
		Assert.assertEquals(PaymentStatus.TIMEOUTED_WAITING_FOR_BTC, orders.get(0).getStatus());
		Assert.assertEquals(PaymentStatus.TIMEOUTED_WAITING_FOR_BTC, orders.get(1).getStatus());
	}
	
	private void cleanPaymentsTable() {
		EntityTransaction tx = emProvider.get().getTransaction();
		tx.begin();
		Session session = emProvider.get().unwrap(Session.class);
		session.createQuery("delete from "+ForeignPaymentOrder.class.getName()).executeUpdate();
		tx.commit();
	}
	
	private void createTwoPreOrders() {
		EntityTransaction tx = emProvider.get().getTransaction();
		tx.begin();
		ForeignPaymentOrder fpo = new ForeignPaymentOrder();
		fpo.setAmount(BigDecimal.TEN);
		fpo.setCreateTimestamp(oldDate);
		fpo.setUpdateTimestamp(oldDate);
		fpo.setStatus(PaymentStatus.WAITING_FOR_BTC);
		emProvider.get().persist(fpo);

		ForeignPaymentOrder fpo2 = new ForeignPaymentOrder();
		fpo2.setAmount(BigDecimal.ONE);
		fpo2.setCreateTimestamp(oldDate);
		fpo2.setUpdateTimestamp(oldDate);
		fpo2.setStatus(PaymentStatus.WAITING_FOR_BTC);
		emProvider.get().persist(fpo2);
		
		tx.commit();
	}	
	
	@SuppressWarnings("unchecked")
	private List<ForeignPaymentOrder> getAllOrders() {
		Session session = emProvider.get().unwrap(Session.class);
		session.clear();
		return session.createCriteria(ForeignPaymentOrder.class).list();
	}
}
