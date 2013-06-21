package com.petrsmid.bitexchange.bitstamp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.petrsmid.bitexchange.GuiceBitexchangeModule;
import com.petrsmid.bitexchange.net.HttpReader;

@RunWith(JUnit4.class)
public class BitstampServiceTest {
	
	public BitstampService getMockedBitstampService() {
		Injector injector = Guice.createInjector(new GuiceBitexchangeModule() {
			@Override
			protected void configureCommons() {
				//mock the HttpReader
				bind(HttpReader.class).to(HttpReaderBitstampMock.class);
			}
		});
		
		return injector.getInstance(BitstampService.class);
	}
	
	public BitstampService getLiveBitstampService() {
		Injector injector = Guice.createInjector(new GuiceBitexchangeModule());
		return injector.getInstance(BitstampService.class);
	}
	
	@Test
	public void testMockedTicker() throws Exception {
		Ticker ticker = getMockedBitstampService().getTicker();
		assertEquals(new BigDecimal("101.11"), ticker.getHigh());
		assertEquals(new BigDecimal("99.99"), ticker.getLast());
		assertEquals(new Long("1371457254"), ticker.getTimestamp());
		assertEquals(new BigDecimal("99.71"), ticker.getBid());
		assertEquals(new BigDecimal("4168.44167661"), ticker.getVolume());
		assertEquals(new BigDecimal("98.00"), ticker.getLow());
		assertEquals(new BigDecimal("99.99"), ticker.getAsk());
	}
	
	@Test
	public void testLiveTicker() throws Exception {
		Ticker ticker = getLiveBitstampService().getTicker();
		assertNotNull(ticker.getHigh());
		assertNotNull(ticker.getLast());
		assertNotNull(ticker.getTimestamp());
		assertNotNull(ticker.getBid());
		assertNotNull(ticker.getVolume());
		assertNotNull(ticker.getLow());
		assertNotNull(ticker.getAsk());
	}
	
	@Test
	public void testMockedOrderBook() throws Exception {
		OrderBook orderBook = getMockedBitstampService().getOrderBook();

		assertEquals(new Long("1371853191"), orderBook.getTimestamp());
		List<List<BigDecimal>> bids = orderBook.getBids();
		assertEquals(2, bids.size());
		List<BigDecimal> bid1 = bids.get(0);
		assertEquals(2, bid1.size());
		assertEquals(new BigDecimal("102.93"), bid1.get(0));
		assertEquals(new BigDecimal("56.32392810"), bid1.get(1));
		List<BigDecimal> bid2 = bids.get(1);
		assertEquals(2, bid2.size());
		
	}
}
