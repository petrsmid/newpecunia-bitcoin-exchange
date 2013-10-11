package com.newpecunia.net;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class HttpReaderFactoryImpl implements HttpReaderFactory {
	
	private RequestCountLimitVerifier requestCountLimitVerifier;
	
	@Inject
	HttpReaderFactoryImpl(RequestCountLimitVerifier requestCountLimitVerifier) {
		this.requestCountLimitVerifier = requestCountLimitVerifier;
	}
	
	@Override
	public HttpReader createNewHttpSimpleReader() {
		return new HttpSimpleReaderImpl(requestCountLimitVerifier);
	}
	
	@Override
	public HttpReader createNewHttpSessionReader() {
		return new HttpSessionReaderImpl(requestCountLimitVerifier);
	}

}
