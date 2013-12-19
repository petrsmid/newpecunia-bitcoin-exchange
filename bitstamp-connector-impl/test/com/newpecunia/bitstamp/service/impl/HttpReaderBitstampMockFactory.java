package com.newpecunia.bitstamp.service.impl;

import com.newpecunia.bitstamp.service.impl.net.HttpReader;
import com.newpecunia.bitstamp.service.impl.net.HttpReaderFactory;

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
