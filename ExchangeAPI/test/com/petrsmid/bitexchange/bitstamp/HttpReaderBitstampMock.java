package com.petrsmid.bitexchange.bitstamp;

import java.io.IOException;

import com.petrsmid.bitexchange.net.HttpReader;

public class HttpReaderBitstampMock implements HttpReader {

	@Override
	public String readUrl(String url) throws IOException {
		if (url.equals(BitstampConstants.TICKER_URL)) {
			return "{" +
					"\"high\": \"101.11\", " +
					"\"last\": \"99.99\", " +
					"\"timestamp\": \"1371457254\", " +
					"\"bid\": \"99.71\", " +
					"\"volume\": \"4168.44167661\", " +
					"\"low\": \"98.00\", " +
					"\"ask\": \"99.99\"" +
					"}";						
		} else {
			return null;
		}
	}

}
