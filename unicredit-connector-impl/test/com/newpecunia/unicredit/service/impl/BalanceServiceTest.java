package com.newpecunia.unicredit.service.impl;

import java.math.BigDecimal;

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
import com.newpecunia.persistence.entities.LastKnownBalance;
import com.newpecunia.unicredit.service.BalanceService;

public class BalanceServiceTest {
	
	private static final BigDecimal LAST_KNOWN_BALANCE = new BigDecimal("123.45");
	private static final BigDecimal FEE = new BigDecimal("0.50");
	private BalanceService balanceService = null;
	private PersistService persistence = null;
	private Provider<EntityManager> emProvider;
	
	@Before
	public void setup() {
		Injector injector = Guice.createInjector(new JpaPersistModule("testingJpaUnit"));
		persistence = injector.getInstance(PersistService.class);
		emProvider = injector.getProvider(EntityManager.class);
		persistence.start();
		balanceService = new BalanceServiceImpl(emProvider, new UnicreditWebdavServiceMock(), new NPConfiguration() {
			@Override
			public void reloadConfiguration() {
				//do nothing
			}
			@Override
			public BigDecimal getPaymentFee() {
				return FEE;
			}
		});
		
		cleanBalanceTable();
		initBalanceTable();
		
		cleanPaymentsTable();
		createTwoUnprocessedPayments();
	}
	
	@After
	public void tearDown() {
		cleanBalanceTable();
		cleanPaymentsTable();
		persistence.stop();
	}

	@Test
	public void getBalanceWhenWebdavReturnsNothing() {
		BigDecimal balance = balanceService.getApproximateBalance();
		BigDecimal expectedBalance = LAST_KNOWN_BALANCE
				.subtract(BigDecimal.TEN).subtract(FEE).subtract(BigDecimal.ONE).subtract(FEE);
		Assert.assertTrue(expectedBalance.compareTo(balance) == 0);
	}
	
	private void initBalanceTable() {
		EntityTransaction tx = emProvider.get().getTransaction();
		tx.begin();
		LastKnownBalance lkb = new LastKnownBalance();
		lkb.setId(1);
		lkb.setBalance(LAST_KNOWN_BALANCE);
		emProvider.get().persist(lkb);
		tx.commit();
	}
	
	private void cleanBalanceTable() {
		EntityTransaction tx = emProvider.get().getTransaction();
		tx.begin();
		LastKnownBalance balance = emProvider.get().find(LastKnownBalance.class, 1);
		if (balance != null) {
			emProvider.get().remove(balance);
		}
		tx.commit();
	}

	private void cleanPaymentsTable() {
		EntityTransaction tx = emProvider.get().getTransaction();
		tx.begin();
		Session session = emProvider.get().unwrap(Session.class);
		session.createQuery("delete from "+ForeignPaymentOrder.class.getName()).executeUpdate();
		tx.commit();
	}
	
	private void createTwoUnprocessedPayments() {
		EntityTransaction tx = emProvider.get().getTransaction();
		tx.begin();
		ForeignPaymentOrder fpo = new ForeignPaymentOrder();
		fpo.setAmount(BigDecimal.TEN);
		fpo.setStatus(PaymentStatus.NEW);
		emProvider.get().persist(fpo);

		ForeignPaymentOrder fpo2 = new ForeignPaymentOrder();
		fpo2.setAmount(BigDecimal.ONE);
		fpo2.setStatus(PaymentStatus.WEBDAV_PENDING);
		emProvider.get().persist(fpo2);
		
		tx.commit();
	}
	
}
