package com.newpecunia.unicredit.service.impl;

import java.math.BigDecimal;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

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
import com.newpecunia.persistence.entities.LastKnownBalance;
import com.newpecunia.unicredit.service.BalanceService;

public class BalanceServiceTest {
	
	private BalanceService balanceService = null;
	private PersistService persistence = null;
	private Provider<EntityManager> emProvider;
	
	@Before
	public void setup() {
		Injector injector = Guice.createInjector(new JpaPersistModule("testingJpaUnit"));
		persistence = injector.getInstance(PersistService.class);
		emProvider = injector.getProvider(EntityManager.class);
		persistence.start();
		balanceService = new BalanceServiceImpl(emProvider, new UnicreditWebdavServiceMock(), new NPConfiguration());
		setupBalanceTable();
	}
	
	@After
	public void tearDown() {
		cleanBalanceTable();
		persistence.stop();
	}

	@Test
	public void getBalance() {
		BigDecimal balance = balanceService.getApproximateBalance();
		Assert.assertNotNull(balance);
	}
	
	private void setupBalanceTable() {
		cleanBalanceTable();
		EntityTransaction tx = emProvider.get().getTransaction();
		tx.begin();
		LastKnownBalance lkb = new LastKnownBalance();
		lkb.setId(1);
		lkb.setBalance(new BigDecimal("123.45"));
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

}
