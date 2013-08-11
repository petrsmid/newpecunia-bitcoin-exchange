package com.newpecunia.net;

public class RequestCountLimitVerifierMock implements RequestCountLimitVerifier {

	@Override
	public void countRequest() throws RequestCountLimitExceededException {
		//do nothing
	}

}
