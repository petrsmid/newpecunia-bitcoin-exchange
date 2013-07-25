package com.newpecunia.bitstamp.service;

import com.newpecunia.net.HttpReader;
import com.newpecunia.net.HttpReaderFactory;

public class HttpReaderBitstampMockFactory implements HttpReaderFactory {

	@Override
	public HttpReader createNewHttpReaderSession() {
		return new HttpReaderBitstampMock();
	}

}
