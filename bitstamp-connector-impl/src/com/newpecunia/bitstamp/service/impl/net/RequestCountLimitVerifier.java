package com.newpecunia.bitstamp.service.impl.net;

public interface RequestCountLimitVerifier {

	void countRequest() throws RequestCountLimitExceededException;

}
