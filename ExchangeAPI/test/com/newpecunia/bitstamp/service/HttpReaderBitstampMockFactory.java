package com.newpecunia.bitstamp.service;

import com.newpecunia.net.HttpReader;
import com.newpecunia.net.HttpReaderFactory;

public class HttpReaderBitstampMockFactory implements HttpReaderFactory {

	private HttpReader httpReaderMock = new HttpReaderBitstampMock();
	
	@Override
	public HttpReader createNewHttpSessionReader() {
		return httpReaderMock;
	}

	@Override
	public HttpReader createNewHttpSimpleReader() {
		return httpReaderMock;
	}

}
