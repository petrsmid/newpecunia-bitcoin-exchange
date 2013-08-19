package com.newpecunia.bitstamp.service;

public class BitstampServiceException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public BitstampServiceException() {
		super();
	}
	
    public BitstampServiceException(String message) {
        super(message);
    }
    
    public BitstampServiceException(String message, Throwable cause) {
        super(message, cause);
    }	
	
    public BitstampServiceException(Throwable cause) {
        super(cause);
    }	

}
