package com.newpecunia.bitstamp.webdriver;

import java.io.IOException;

public interface BitstampSession {

	boolean isWaitingForDeposit() throws IOException, BitstampWebdriverException;

	void createInternationalUSDDeposit(int amount, String name, String surname, String comment) throws IOException, BitstampWebdriverException;

	void cancelLastDeposit() throws IOException, BitstampWebdriverException;

}
