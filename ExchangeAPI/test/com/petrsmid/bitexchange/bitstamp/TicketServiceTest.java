package com.petrsmid.bitexchange.bitstamp;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
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
public class TicketServiceTest {
	
	private Injector injector = null;
	@Before
	public void setup() {
		injector = Guice.createInjector(new GuiceBitexchangeModule() {
			@Override
			protected void configureCommons() {
				//mock of the HttpReader
				bind(HttpReader.class).toInstance(new HttpReader() {
					@Override
					public String readUrl(String url) throws IOException {
						return "{" +
								"\"high\": \"101.11\", " +
								"\"last\": \"99.99\", " +
								"\"timestamp\": \"1371457254\", " +
								"\"bid\": \"99.71\", " +
								"\"volume\": \"4168.44167661\", " +
								"\"low\": \"98.00\", " +
								"\"ask\": \"99.99\"" +
								"}";						
					}
				});
			}
		});		
	}
	
	@Test
	public void testTicketService() throws Exception {
		TickerService tickerService = injector.getInstance(TickerService.class);
		Ticker ticker = tickerService.getTicker();
		assertEquals(new BigDecimal("101.11"), ticker.getHigh());
		assertEquals(new BigDecimal("99.99"), ticker.getLast());
		assertEquals(new Long("1371457254"), ticker.getTimestamp());
		assertEquals(new BigDecimal("99.71"), ticker.getBid());
		assertEquals(new BigDecimal("4168.44167661"), ticker.getVolume());
		assertEquals(new BigDecimal("98.00"), ticker.getLow());
		assertEquals(new BigDecimal("99.99"), ticker.getAsk());
	}
}
