package com.newpecunia.net;

public class RequestCountLimitExceededException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public RequestCountLimitExceededException() {
		super();
	}
	
	public RequestCountLimitExceededException(String msg) {
		super(msg);
	}

}
