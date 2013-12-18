package com.newpecunia.bitstamp.webdriver.impl;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.newpecunia.bitstamp.webdriver.BitstampWebdriver;
import com.newpecunia.bitstamp.webdriver.BitstampWebdriverException;
import com.newpecunia.bitstamp.webdriver.InternationalWithdrawRequest;
import com.newpecunia.bitstamp.webdriver.WithdrawOverviewLine;
import com.newpecunia.configuration.NPCredentials;
import com.newpecunia.net.HttpReaderFactory;
import com.newpecunia.synchronization.LockProvider;

@Singleton
public class BitstampWebdriverImpl implements BitstampWebdriver {

	private static final Logger logger = LogManager.getLogger(BitstampWebdriverImpl.class);	
	
	private static final long MAX_SESSION_AGE_MS = 10*60*1000; //ten minutes (in milliseconds)
	
	private NPCredentials credentials;
	private HttpReaderFactory httpReaderFactory;
	private LockProvider lockProvider;
	
	private long startTimeOfActiveSession = 0;
	private BitstampSession activeBitstampSession = null;
	
	
	@Inject
	BitstampWebdriverImpl(HttpReaderFactory httpReaderFactory, NPCredentials credentials, LockProvider lockProvider) {
		this.httpReaderFactory = httpReaderFactory;
		this.credentials = credentials;
		this.lockProvider = lockProvider;
	}
	
	private BitstampSession getActiveSession() throws BitstampWebdriverException, IOException {
		long actualTime = System.currentTimeMillis();
		if ((actualTime - startTimeOfActiveSession) >= MAX_SESSION_AGE_MS) {
			activeBitstampSession = BitstampSession.createSession(httpReaderFactory, credentials, lockProvider); 
			startTimeOfActiveSession = actualTime;
		}
		return activeBitstampSession;
	}
	
	@Override
	public boolean isWaitingForDeposit() throws IOException, BitstampWebdriverException {
		logger.trace("Checking whether is Bitstamp in status waiting for a deposit.");
		return getActiveSession().isWaitingForDeposit();
	}

	@Override
	public void createInternationalUSDDeposit(int amount, String name, String surname, String comment) throws IOException, BitstampWebdriverException {
		logger.trace(String.format("Creating deposit: %s USD, %s, %s, %s", amount, name, surname, comment));
		getActiveSession().createInternationalUSDDeposit(amount, name, surname, comment);
	}

	@Override
	public void cancelLastDeposit() throws IOException, BitstampWebdriverException {
		logger.trace("Canceling last deposit.");
		getActiveSession().cancelLastDeposit();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long createInternationalWithdraw(InternationalWithdrawRequest request) throws IOException, BitstampWebdriverException {
		logger.trace(String.format("Withdrawing: %s %s  to IBAN: %s, BIC: %s", 
				request.getAmount().toPlainString(), request.getCurrency(), request.getIban(), request.getBic()));
		return getActiveSession().createInternationalWithdraw(request);
	}

	@Override
	public List<WithdrawOverviewLine> getWithdrawOverview() throws IOException, BitstampWebdriverException {
		logger.trace("Getting withdrawal overview.");
		return getActiveSession().getWithdrawOverview();
	}

	@Override
	public void cancelWithdraw(long id) throws IOException, BitstampWebdriverException {
		logger.trace("Canceling withdrawal "+id);
		getActiveSession().cancelWithdraw(id);		
	}
}
