package com.newpecunia.bitstamp.service.impl;

import java.io.IOException;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.NameValuePair;

import com.newpecunia.bitstamp.service.impl.BitstampServiceConstants;
import com.newpecunia.net.HttpReader;
import com.newpecunia.net.HttpReaderOutput;

public class HttpReaderBitstampMock implements HttpReader {

	@Override
	public String get(String url) throws IOException {
		if (url.equals(BitstampServiceConstants.TICKER_URL)) {
			return "{" +
					"\"high\": \"101.11\", " +
					"\"last\": \"99.99\", " +
					"\"timestamp\": \"1371457254\", " +
					"\"bid\": \"99.71\", " +
					"\"volume\": \"4168.44167661\", " +
					"\"low\": \"98.00\", " +
					"\"ask\": \"99.99\"" +
					"}";						
		} else if (url.equals(BitstampServiceConstants.ORDER_BOOK_URL)) {
			return "{" +
					"\"timestamp\": \"1371853191\", " +
					"\"bids\": [[\"102.93\", \"56.32392810\"], [\"102.91\", \"4.07422019\"]], " +
					"\"asks\": [[\"102.95\", \"1.00000000\"], [\"102.96\", \"1.00000001\"]]" +
					"}";
		} else {
			return null;
		}
	}

	@Override
	public String post(String url, List<NameValuePair> params) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String get(String url, List<Header> headers) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String post(String url, List<Header> headers,
			List<NameValuePair> params) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpReaderOutput getWithMetadata(String url, List<Header> headers)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpReaderOutput postWithMetadata(String url, List<Header> headers,
			List<NameValuePair> params) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpReaderOutput getWithMetadata(String url) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
