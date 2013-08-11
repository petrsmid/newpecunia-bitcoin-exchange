package com.newpecunia.bitstamp;

import org.junit.Test;

import com.newpecunia.net.RequestCountLimitExceededException;
import com.newpecunia.util.TimeProviderMock;

public class BitstampRequestCountLimitVerifierTest {

	
	@Test(expected=RequestCountLimitExceededException.class)
	public void testCountExceeded() {
		BitstampRequestCountLimitVerifier countVerifier = new BitstampRequestCountLimitVerifier(new TimeProviderMock());
		for (int i = 0; i < 10000; i++) {
			countVerifier.countRequest();
		}
		//exception should be thrown
	}
	
	@Test
	public void testCounterReseted() {
		TimeProviderMock timeProviderMock = new TimeProviderMock();
		BitstampRequestCountLimitVerifier countVerifier = new BitstampRequestCountLimitVerifier(timeProviderMock);
		
		timeProviderMock.setTime(0);
		for (int i = 0; i < 400; i++) {
			countVerifier.countRequest();
		}
		
		timeProviderMock.setTime(8*60*1000); //after 8 minutes
		for (int i = 0; i < 150; i++) {
			countVerifier.countRequest();
		}
		
		timeProviderMock.setTime(12*60*1000); //after 12 minutes
		for (int i = 0; i < 400; i++) {
			countVerifier.countRequest();
		}

		//no exception should be thrown
	}
}
