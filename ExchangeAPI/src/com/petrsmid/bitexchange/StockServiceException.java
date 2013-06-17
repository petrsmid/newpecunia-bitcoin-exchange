package com.petrsmid.bitexchange;

public class StockServiceException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public StockServiceException() {
		super();
	}
	
    public StockServiceException(String message) {
        super(message);
    }
    
    public StockServiceException(String message, Throwable cause) {
        super(message, cause);
    }	
	
    public StockServiceException(Throwable cause) {
        super(cause);
    }	

}
