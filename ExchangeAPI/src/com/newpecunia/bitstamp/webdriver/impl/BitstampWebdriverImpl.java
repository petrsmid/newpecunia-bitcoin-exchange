package com.newpecunia.bitstamp.webdriver.impl;

import java.io.IOException;

import com.google.inject.Inject;
import com.newpecunia.bitstamp.service.impl.BitstampCredentials;
import com.newpecunia.bitstamp.webdriver.BitstampSession;
import com.newpecunia.bitstamp.webdriver.BitstampWebdriver;
import com.newpecunia.bitstamp.webdriver.BitstampWebdriverException;
import com.newpecunia.net.HttpReaderFactory;
import com.newpecunia.synchronization.ClusterLockProvider;

public class BitstampWebdriverImpl implements BitstampWebdriver {

	private BitstampCredentials credentials;
	private HttpReaderFactory httpReaderFactory;
	private ClusterLockProvider lockProvider;

	@Inject
	public BitstampWebdriverImpl(HttpReaderFactory httpReaderFactory, BitstampCredentials credentials, ClusterLockProvider lockProvider) {
		this.httpReaderFactory = httpReaderFactory;
		this.credentials = credentials;
		this.lockProvider = lockProvider;
	}
	
	@Override
	public BitstampSession createSession() throws BitstampWebdriverException {
		try {
			return BitstampSessionImpl.createSession(httpReaderFactory, credentials, lockProvider);
		} catch (IOException e) {
			throw new BitstampWebdriverException(e);
		}
	}
}
