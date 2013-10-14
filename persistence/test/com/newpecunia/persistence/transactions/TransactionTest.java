package com.newpecunia.persistence.transactions;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;

public class TransactionTest {

	private TestService testService;
	Injector injector = null;
	PersistService persistService;
	
	@Before
	public void setup() {
		injector = Guice.createInjector(new JpaPersistModule("productionJpaUnit"), new TestModule());
		persistService  = injector.getInstance(PersistService.class);
		testService  = injector.getInstance(TestService.class);
		
		persistService.start();
		deleteAll();
	}
	
	@After
	public void tearDown() {
		deleteAll();
		persistService.stop();
	}
	
	@Test
	public void testBothSeparatelyOK() {
		try {
			testService.transactionalMethod1(false);
			testService.transactionalMethod2(false);
		}catch (Exception e) {}
		Assert.assertEquals(1, testService.getEntitiesFromMethod1().size());
		Assert.assertEquals(1, testService.getEntitiesFromMethod2().size());
	}

	@Test
	public void testBothSeparately2ndKO() {
		try {
			testService.transactionalMethod1(false);
			testService.transactionalMethod2(true);
		}catch (Exception e) {}
		Assert.assertEquals(1, testService.getEntitiesFromMethod1().size());
		Assert.assertEquals(0, testService.getEntitiesFromMethod2().size());
	}
	
	@Test
	public void testBothTogetherOK() {
		try {
			testService.transactionalMethod1And2(false, false);
		}catch (Exception e) {}
		Assert.assertEquals(1, testService.getEntitiesFromMethod1().size());
		Assert.assertEquals(1, testService.getEntitiesFromMethod2().size());
	}

	@Test
	public void testBothTogetherSecondKO() {
		try {
			testService.transactionalMethod1And2(false, true);
		}catch (Exception e) {}
		Assert.assertEquals(0, testService.getEntitiesFromMethod1().size());
		Assert.assertEquals(0, testService.getEntitiesFromMethod2().size());
	}
	
	@Test
	public void testBothTogetherNONTransactionallyOK() {
		try {
			testService.nonTransactionalMethod1And2(false, false);
		}catch (Exception e) {}
		Assert.assertEquals(1, testService.getEntitiesFromMethod1().size());
		Assert.assertEquals(1, testService.getEntitiesFromMethod2().size());
	}
	
	@Test
	public void testBothTogetherNONTransactionally2ndKO() {
		try {
			testService.nonTransactionalMethod1And2(false, true);
		}catch (Exception e) {}
		Assert.assertEquals(1, testService.getEntitiesFromMethod1().size());
		Assert.assertEquals(0, testService.getEntitiesFromMethod2().size());
	}
	
	private void deleteAll() {
		testService.deleteAll();
	}
}
