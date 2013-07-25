package com.newpecunia.net;

public class HttpReaderFactoryImpl implements HttpReaderFactory {
	
	@Override
	public HttpReader createNewHttpSimpleReader() {
		return new HttpSimpleReaderImpl();
	}
	
	@Override
	public HttpReader createNewHttpSessionReader() {
		return new HttpSessionReaderImpl();
	}

}
