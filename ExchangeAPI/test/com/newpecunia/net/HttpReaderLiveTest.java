package com.newpecunia.net;


import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.newpecunia.bitstamp.service.impl.BitstampServiceConstants;

@RunWith(JUnit4.class)
public class HttpReaderLiveTest {

	HttpReader httpReader = new HttpSimpleReaderImpl();
	
	@Test
	public void test() throws IOException {
		String output = httpReader.get(BitstampServiceConstants.TICKER_URL);
		assertTrue(output.startsWith("{"));
		assertTrue(output.endsWith("}"));
	}
}
