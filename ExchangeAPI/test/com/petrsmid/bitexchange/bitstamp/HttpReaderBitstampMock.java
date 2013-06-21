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
		} else if (url.equals(BitstampConstants.ORDER_BOOK_URL)) {
			return "{" +
					"\"timestamp\": \"1371853191\", " +
					"\"bids\": [[\"102.93\", \"56.32392810\"], [\"102.91\", \"4.07422019\"]], " +
					"\"asks\": [[\"102.95\", \"1.00000000\"], [\"102.96\", \"1.00000000\"]]" +
					"}";
		} else {
			return null;
		}
	}

}
