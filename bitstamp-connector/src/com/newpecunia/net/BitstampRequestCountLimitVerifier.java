package com.newpecunia.net;

import java.util.ArrayDeque;
import java.util.Deque;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;
import com.newpecunia.util.TimeProvider;

public class BitstampRequestCountLimitVerifier implements RequestCountLimitVerifier {
	
	private static final long TIME_WINDOW = 10*60*1000; //10 minutes
	private static final int MAX_REQUESTS_COUNT = 600;
	
	private final Logger logger = LogManager.getLogger(BitstampRequestCountLimitVerifier.class);
	
	private TimeProvider timeProvider;
	private Deque<Long> requestTimestamps = new ArrayDeque<>();
	
	@Inject
	public BitstampRequestCountLimitVerifier(TimeProvider timeProvider) {
		this.timeProvider = timeProvider;
	}
	
	@Override
	public synchronized void countRequest() throws RequestCountLimitExceededException {
		removeRequestsOlderThanWindow();
		if (requestTimestamps.size() > (MAX_REQUESTS_COUNT / 2)) {
			logger.warn("A huge amount of requests is performed to Bitstamp service!");
		}
		if (requestTimestamps.size() >= MAX_REQUESTS_COUNT) {
			logger.error("Number of requests exceeded the limit of Bitstamp service! Disabling calling Bitstamp service to prevent ban!");
			throw new RequestCountLimitExceededException("Number of requests exceeded the limit of Bitstamp service.");
		}
		
		requestTimestamps.push(timeProvider.now());
	}

	private void removeRequestsOlderThanWindow() {
		long now = timeProvider.now();		
		while (!requestTimestamps.isEmpty() && 
				((now - requestTimestamps.getLast()) > TIME_WINDOW)) {
			requestTimestamps.pollLast();
		}
	}

}
