package com.newpecunia.net;

public class HttpReaderFactoryImpl implements HttpReaderFactory {
	
	@Override
	public HttpReader createNewHttpReaderSession() {
		return new HttpReaderImpl();
	}

}
