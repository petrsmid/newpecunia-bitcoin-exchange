package com.newpecunia.bitstamp.webdriver;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.newpecunia.GuiceBitexchangeModule;

public class BitstampWebdriverLiveTest {

	private BitstampWebdriver bitstampWebdriver = null;

	
	@Before
	public void setup() {
		Injector injector = Guice.createInjector(new GuiceBitexchangeModule());
		bitstampWebdriver = injector.getInstance(BitstampWebdriver.class);
	}

	@Test
	public void testLiveLogin() throws Exception {
		BitstampSession session = bitstampWebdriver.createSession();
		Assert.assertNotNull(session);
	}
	
	@Test
	public void testLiveIsWaitingForDeposit() throws Exception {
		BitstampSession session = bitstampWebdriver.createSession();
		session.isWaitingForDeposit();
		//no until now -> OK
	}
	
	@Test
	public void testLiveCreateDeposit() throws Exception {
		BitstampSession session = bitstampWebdriver.createSession();
		session.createInternationalUSDDeposit(new BigDecimal("51"), "Petr", "Smid", "this is a test");
	}
	
	@Test
	public void testCancelLastDeposit() throws Exception {
		BitstampSession session = bitstampWebdriver.createSession();
		session.cancelLastDeposit();
		//no until now -> OK
	}
}
