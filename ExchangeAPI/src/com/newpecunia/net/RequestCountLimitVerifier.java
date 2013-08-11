package com.newpecunia.net;

public interface RequestCountLimitVerifier {

	void countRequest() throws RequestCountLimitExceededException;

}
