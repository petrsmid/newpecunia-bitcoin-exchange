package com.newpecunia.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class ThrottlingSMTPAppenderTest {

	private static final Logger npLogger = LogManager.getLogger(ThrottlingSMTPAppenderTest.class);	
	private static final Logger foreignLogger = LogManager.getLogger("blabla");	
	
	@Test
	public void testLogger() {
		try {
			throw new RuntimeException("Test of exception");
		} catch (Exception e) {
			npLogger.trace("New Pecunia: Test of tracing");
			npLogger.error("New Pecunia: Test of error", e);
			foreignLogger.trace("Foreign: Test of tracing");
			foreignLogger.error("Foreign: Test of error", e);
		}
	}
	
	@Test
	public void testThrottling() {
		npLogger.error("Test error 1");
		foreignLogger.error("Test error 2 should not be sen by email");
	}
}
