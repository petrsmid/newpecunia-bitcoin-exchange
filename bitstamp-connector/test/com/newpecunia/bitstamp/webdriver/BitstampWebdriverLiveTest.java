package com.newpecunia.bitstamp.webdriver;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.newpecunia.bitstamp.BitstampConnectorModule;
import com.newpecunia.common.CommonModule;

public class BitstampWebdriverLiveTest {

	private BitstampWebdriver bitstampWebdriver = null;

	
	@Before
	public void setup() {
		Injector injector = Guice.createInjector(new BitstampConnectorModule(), new CommonModule());
		bitstampWebdriver = injector.getInstance(BitstampWebdriver.class);
	}
	
	@Test
	public void testLiveIsWaitingForDeposit() throws Exception {
		bitstampWebdriver.isWaitingForDeposit();
		//no Exception until now -> OK
	}
	
	@Test
	public void testCancelLastDeposit() throws Exception {
		bitstampWebdriver.cancelLastDeposit();
		//no Exception until now -> OK
	}

	@Test
	public void testLiveCreateCheckAndCancelDeposit() throws Exception {
		//CREATE
		bitstampWebdriver.createInternationalUSDDeposit(51, "Petr", "Smid", "this is a test");
		
		//CHECK WHETHER IT EXISTS
		//no Exception until now -> OK
		boolean isWaiting = bitstampWebdriver.isWaitingForDeposit();
		Assert.assertTrue(isWaiting);
		
		//CANCEL
		bitstampWebdriver.cancelLastDeposit();
		//no Exception until now -> OK
	}
	
	@Test
	public void testWithdrawOverview() throws Exception {
		List<WithdrawOverviewLine> overview = bitstampWebdriver.getWithdrawOverview();
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

	
	private Long createWithdraw() throws Exception {
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

		Long withdrawId = bitstampWebdriver.createInternationalWithdraw(request);
		Assert.assertNotNull(withdrawId);
		return withdrawId;
	}
	
	
	private void cancelWithdraw(Long id) throws Exception {
		bitstampWebdriver.cancelWithdraw(id);		
		//no Exception until now -> OK
	}
	
	@Test
	public void testCreateAndCancelWithdraw() throws Exception {
		Long id = createWithdraw();
		cancelWithdraw(id);		
	}
	
}
