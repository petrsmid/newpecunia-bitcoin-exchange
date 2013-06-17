package com.petrsmid.bitexchange.bitstamp;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.petrsmid.bitexchange.GuiceBitexchangeModule;
import com.petrsmid.bitexchange.net.HttpReader;

@RunWith(JUnit4.class)
public class BitstampServiceTest {
	
	private BitstampService bitstampService;
	
	@Before
	public void setup() {
		Injector injector = Guice.createInjector(new GuiceBitexchangeModule() {
			@Override
			protected void configureCommons() {
				//mock the HttpReader
				bind(HttpReader.class).to(HttpReaderBitstampMock.class);
			}
		});
		
		bitstampService = injector.getInstance(BitstampService.class);
	}
	
	@Test
	public void testTicker() throws Exception {
		Ticker ticker = bitstampService.getTicker();
		assertEquals(new BigDecimal("101.11"), ticker.getHigh());
		assertEquals(new BigDecimal("99.99"), ticker.getLast());
		assertEquals(new Long("1371457254"), ticker.getTimestamp());
		assertEquals(new BigDecimal("99.71"), ticker.getBid());
		assertEquals(new BigDecimal("4168.44167661"), ticker.getVolume());
		assertEquals(new BigDecimal("98.00"), ticker.getLow());
		assertEquals(new BigDecimal("99.99"), ticker.getAsk());
	}
}
