package com.newpecunia.bitstamp.service.impl.net;

public class RequestCountLimitVerifierMock implements RequestCountLimitVerifier {

	@Override
	public void countRequest() throws RequestCountLimitExceededException {
		//do nothing
	}

}
