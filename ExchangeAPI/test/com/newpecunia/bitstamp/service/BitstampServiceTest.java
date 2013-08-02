package com.newpecunia.bitstamp.service;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.newpecunia.bitstamp.service.impl.BitstampServiceImpl;

@RunWith(JUnit4.class)
public class BitstampServiceTest {
	
	private BitstampService getMockedBitstampService() {
		return new BitstampServiceImpl(new HttpReaderBitstampMockFactory(), new BitstampCredentialsMock());
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
	public void testMockedOrderBook() throws Exception {
		OrderBook orderBook = getMockedBitstampService().getOrderBook();

		assertEquals(new Long("1371853191"), orderBook.getTimestamp());
		List<PriceAndAmount> bids = orderBook.getBids();
		assertEquals(2, bids.size());
		PriceAndAmount bid1 = bids.get(0);
		assertEquals(new BigDecimal("102.93"), bid1.getPrice());
		assertEquals(new BigDecimal("56.32392810"), bid1.getAmount());
		PriceAndAmount bid2 = bids.get(1);
		assertEquals(new BigDecimal("102.91"), bid2.getPrice());
		assertEquals(new BigDecimal("4.07422019"), bid2.getAmount());
		
		List<PriceAndAmount> asks = orderBook.getAsks();
		assertEquals(2, asks.size());
		PriceAndAmount ask1 = asks.get(0);
		assertEquals(new BigDecimal("102.95"), ask1.getPrice());
		assertEquals(new BigDecimal("1.00000000"), ask1.getAmount());

		PriceAndAmount ask2 = asks.get(1);
		assertEquals(new BigDecimal("102.96"), ask2.getPrice());
		assertEquals(new BigDecimal("1.00000001"), ask2.getAmount());
	}
	
	
	
}
