package com.petrsmid.bitexchange.bitstamp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.petrsmid.bitexchange.GuiceBitexchangeModule;

public class BitstampServiceLiveTest {

	private BitstampService bitstampService = null;

	@Before
	public void warning() {
		Injector injector = Guice.createInjector(new GuiceBitexchangeModule());
		bitstampService = injector.getInstance(BitstampService.class);
	}

	@Test
	public void testLiveTicker() throws Exception {
		Ticker ticker = bitstampService.getTicker();
		assertNotNull(ticker.getHigh());
		assertNotNull(ticker.getLast());
		assertNotNull(ticker.getTimestamp());
		assertNotNull(ticker.getBid());
		assertNotNull(ticker.getVolume());
		assertNotNull(ticker.getLow());
		assertNotNull(ticker.getAsk());
	}
	
	@Test
	public void testLiveBuyLimitOrder() throws Exception {
		//CAUTION: the following live test will perform REAL transaction with REAL money!

		if (false) { //set to true if you want to run the test
			bitstampService.buyLimitOrder(new BigDecimal("0.1"), new BigDecimal("10"));
		}
	}
	
	@Test
	public void testLiveSellLimitOrder() throws Exception {
		//CAUTION: the following live test will perform REAL transaction with REAL money!

		if (false) { //set to true if you want to run the test
			bitstampService.sellLimitOrder(new BigDecimal("10000"), new BigDecimal("1"));
		}
	}	
	
	@Test
	public void testLiveCancelOrder() throws Exception {
		//CAUTION: the following live test will perform REAL transaction with REAL money!

		if (false) { //set to true if you want to run the test
			bitstampService.cancelOrder("123"); //TODO set here your order ID
		}
	}

	@Test
	public void testLiveGetOpenOrders() throws Exception {
		List<Order> orders = bitstampService.getOpenOrders();
	}
	
	@Test
	public void testBuySellGetCancel() throws Exception {
		//CAUTION: the following live test will perform REAL transactions. However probably without loosing money.

		if (true) { //set to true if you want to run the test
			Order order = bitstampService.sellLimitOrder(new BigDecimal("10000"), new BigDecimal("0.01"));
			List<Order> orders = bitstampService.getOpenOrders();
			assertEquals(1, orders.size());
			assertEquals(order.getId(), orders.get(0).getId());
			boolean result = bitstampService.cancelOrder(order.getId());
			assertTrue(result);
			orders = bitstampService.getOpenOrders();
			assertEquals(0, orders.size());
		}
	}
	
	//@Test
	public void cleanAllOpenedOrders() throws Exception {
		//CAUTION: NEVER run this test with PRODUCTION account! You would DELETE ALL opened transactions! 
		//  run it ONLY if you want to clean up all orders of your TEST account
		if (false) {
			List<Order> orders = bitstampService.getOpenOrders();
			for (Order order : orders) {
				bitstampService.cancelOrder(order.getId());			
			}
			orders = bitstampService.getOpenOrders();
			assertEquals(0, orders.size());
		}
	}
	
	
}
