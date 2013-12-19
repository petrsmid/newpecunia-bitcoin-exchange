package com.newpecunia.bitstamp.service.impl.net;


import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.newpecunia.bitstamp.service.impl.net.BitstampRequestCountLimitVerifier;
import com.newpecunia.bitstamp.service.impl.net.HttpReader;
import com.newpecunia.bitstamp.service.impl.net.HttpSimpleReaderImpl;
import com.newpecunia.time.TimeProviderImpl;

@RunWith(JUnit4.class)
public class HttpReaderLiveTest {

	HttpReader httpReader = new HttpSimpleReaderImpl(new BitstampRequestCountLimitVerifier(new TimeProviderImpl()));
	
	@Test
	public void test() throws IOException {
		String output = httpReader.get("http://www.google.com");
		assertTrue(output.toLowerCase().contains("html"));
	}
}
