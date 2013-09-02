package com.newpecunia;

/**
 * Exception which can be thrown only because of a mistake in program.
 */
public class ProgrammerException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public ProgrammerException() {
		super();
	}
	
	public ProgrammerException(String msg) {
		super(msg);
	}
	
	public ProgrammerException(Exception cause) {
		super(cause);
	}
	
	public ProgrammerException(String msg, Exception cause) {
		super(msg, cause);
	}
}
