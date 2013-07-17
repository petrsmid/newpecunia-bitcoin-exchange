package com.newpecunia.net;


import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.newpecunia.GuiceBitexchangeModule;
import com.newpecunia.bitstamp.service.impl.BitstampConstants;
import com.newpecunia.net.HttpReader;

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
		String output = httpReader.get(BitstampConstants.TICKER_URL);
		assertTrue(output.startsWith("{"));
		assertTrue(output.endsWith("}"));
	}
}
