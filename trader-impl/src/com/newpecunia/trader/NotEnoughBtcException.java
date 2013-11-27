package com.newpecunia.trader;

public class NotEnoughBtcException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public NotEnoughBtcException() {super();}
	public NotEnoughBtcException(String msg) {super(msg);}
	public NotEnoughBtcException(Exception cause) {super(cause);}
	public NotEnoughBtcException(String msg, Exception cause) {super(msg, cause);}
	
}
