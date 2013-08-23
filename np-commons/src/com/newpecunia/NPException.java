package com.newpecunia;

public class NPException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public NPException() {
		super();
	}
	
	public NPException(String msg) {
		super(msg);
	}
	
	public NPException(Exception cause) {
		super(cause);
	}
	
	public NPException(String msg, Exception cause) {
		super(msg, cause);
	}

}
