package com.petrsmid.bitexchange.net;


import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.petrsmid.bitexchange.GuiceBitexchangeModule;
import com.petrsmid.bitexchange.bitstamp.BitstampConstants;

@RunWith(JUnit4.class)
public class HttpReaderLiveTest {

	HttpReader httpReader;
	
	@Before
	public void setup() {
		Injector injector = Guice.createInjector(new GuiceBitexchangeModule());
		httpReader = injector.getInstance(HttpReader.class);
	}
	
	@Test
	public void test() throws IOException {
		String output = httpReader.readUrl(BitstampConstants.TICKER_URL);
		assertTrue(output.startsWith("{"));
		assertTrue(output.endsWith("}"));
	}
}
