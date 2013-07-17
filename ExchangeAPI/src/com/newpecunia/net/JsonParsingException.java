package com.newpecunia.net;

public class JsonParsingException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public JsonParsingException(Throwable cause) {
		super(cause);
	}
	
	public JsonParsingException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
