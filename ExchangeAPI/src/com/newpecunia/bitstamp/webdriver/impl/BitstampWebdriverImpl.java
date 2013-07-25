package com.newpecunia.bitstamp.webdriver.impl;

import java.io.IOException;

import com.google.inject.Inject;
import com.newpecunia.bitstamp.service.impl.BitstampCredentials;
import com.newpecunia.bitstamp.webdriver.BitstampSession;
import com.newpecunia.bitstamp.webdriver.BitstampWebdriver;
import com.newpecunia.bitstamp.webdriver.BitstampWebdriverException;
import com.newpecunia.net.HttpReaderFactory;

public class BitstampWebdriverImpl implements BitstampWebdriver {

	private BitstampCredentials credentials;
	private HttpReaderFactory httpReaderFactory;

	@Inject
	public BitstampWebdriverImpl(HttpReaderFactory httpReaderFactory, BitstampCredentials credentials) {
		this.httpReaderFactory = httpReaderFactory;
		this.credentials = credentials;
	}
	
	@Override
	public BitstampSession createSession() throws BitstampWebdriverException {
		try {
			return BitstampSessionImpl.createSession(httpReaderFactory, credentials);
		} catch (IOException e) {
			throw new BitstampWebdriverException(e);
		}
	}
}
