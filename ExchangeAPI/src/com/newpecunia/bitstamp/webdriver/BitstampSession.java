package com.newpecunia.bitstamp.webdriver;

import java.io.IOException;
import java.util.List;

public interface BitstampSession {

	boolean isWaitingForDeposit() throws IOException, BitstampWebdriverException;

	void createInternationalUSDDeposit(int amount, String name, String surname, String comment) throws IOException, BitstampWebdriverException;

	void cancelLastDeposit() throws IOException, BitstampWebdriverException;

	/**
	 * Create international withdraw request.
	 * !!! Important - e-mail confirmation is needed to perform otherwise the request would be canceled after one hour.
	 * @return ID of the request - from withdraw overview table
	 */
	Long createInternationalWithdraw(InternationalWithdrawRequest request) throws IOException, BitstampWebdriverException;

	List<WithdrawOverviewLine> getWithdrawOverview() throws IOException, BitstampWebdriverException;

	void cancelWithdraw(long id);
}
