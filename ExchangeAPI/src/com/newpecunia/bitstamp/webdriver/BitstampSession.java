package com.newpecunia.bitstamp.webdriver;

import java.io.IOException;
import java.util.List;

public interface BitstampSession {

	boolean isWaitingForDeposit() throws IOException, BitstampWebdriverException;

	void createInternationalUSDDeposit(int amount, String name, String surname, String comment) throws IOException, BitstampWebdriverException;

	void cancelLastDeposit() throws IOException, BitstampWebdriverException;

	void createInternationalWithdraw(InternationalWithdrawRequest request) throws IOException, BitstampWebdriverException;

	List<WithdrawOverviewLine> getWithdrawOverview() throws IOException, BitstampWebdriverException;

}
