package com.newpecunia.bitstamp.webdriver;

public class BitstampWebdriverException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public BitstampWebdriverException() {
		super();
	}
	
    public BitstampWebdriverException(String message) {
        super(message);
    }
    
    public BitstampWebdriverException(String message, Throwable cause) {
        super(message, cause);
    }	
	
    public BitstampWebdriverException(Throwable cause) {
        super(cause);
    }	

	
}
