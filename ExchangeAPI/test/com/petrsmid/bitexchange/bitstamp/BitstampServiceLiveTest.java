package com.petrsmid.bitexchange.bitstamp;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.petrsmid.bitexchange.GuiceBitexchangeModule;
import com.petrsmid.bitexchange.bitstamp.impl.dto.Ticker;

public class BitstampServiceLiveTest {

	private BitstampService bitstampService = null;

	/**
	 * Only when accepting the risks with "yes" the test will be performed
	 */
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
		//CAUTION: the following live test will perform REAL transaction with REAL Bitcoins!

		if (true) { //set to true if you want to run the test
			bitstampService.buyLimitOrder(new BigDecimal("0.1"), new BigDecimal("1"));
		}
	}
	
	
}
