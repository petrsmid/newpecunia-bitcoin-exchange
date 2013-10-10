package com.newpecunia.bitcoind.service;

public class BitcoindException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public BitcoindException(String message) {
		super(message);
	}

	public BitcoindException(String message, Exception cause) {
		super(message, cause);
	}

}
