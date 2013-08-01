package com.newpecunia.bitstamp.webdriver;

import java.math.BigDecimal;
import java.util.List;

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
		//no Exception until now -> OK
	}
	
	@Test
	public void testLiveCreateDeposit() throws Exception {
		BitstampSession session = bitstampWebdriver.createSession();
		session.createInternationalUSDDeposit(51, "Petr", "Smid", "this is a test");
		//no Exception until now -> OK
	}
	
	@Test
	public void testCancelLastDeposit() throws Exception {
		BitstampSession session = bitstampWebdriver.createSession();
		session.cancelLastDeposit();
		//no Exception until now -> OK
	}
	
	@Test
	public void testWithdraw() throws Exception {
		BitstampSession session = bitstampWebdriver.createSession();
		InternationalWithdrawRequest request = new InternationalWithdrawRequest();
		request.setAmount(new BigDecimal("50"));
		request.setCurrency("USD");
		request.setName("Test Name");
		request.setAddress("Street 123");
		request.setCity("Chotin");
		request.setPostalCode("12345");
		request.setCountry("SK");
		
		request.setIban("ABCDEFGH00TEST00123456789");
		request.setBic("BICTESTBIC");
		
		request.setBankName("Test Bank Ica");
		request.setBankCity("Chotin");
		request.setBankAddress("Jazerna 420");
		request.setBankPostalCode("7654321");
		request.setBankCountry("SK");

		session.createInternationalWithdraw(request);
		//no Exception until now -> OK
	}
	
	@Test
	public void testWithdrawOverview() throws Exception {
		BitstampSession session = bitstampWebdriver.createSession();
		List<WithdrawOverviewLine> overview = session.getWithdrawOverview();		
		Assert.assertNotNull(overview);
		WithdrawOverviewLine lastLine = null;
		for (WithdrawOverviewLine overviewLine : overview) {
			Assert.assertNotNull(overviewLine.getDescription());
			Assert.assertNotNull(overviewLine.getAmount());
			Assert.assertNotNull(overviewLine.getDate());
			Assert.assertNotNull(overviewLine.getId());
			Assert.assertNotNull(overviewLine.getStatus());
			Assert.assertNotNull(overviewLine.getWithdrawType());
			if (lastLine != null) {
				Assert.assertTrue(lastLine.getId() > overviewLine.getId());
			}
			lastLine = overviewLine;
		}
		
	}
	
}
