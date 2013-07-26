package com.newpecunia.bitstamp.webdriver;

import java.io.IOException;
import java.math.BigDecimal;

public interface BitstampSession {

	boolean isWaitingForDeposit() throws IOException, BitstampWebdriverException;

	void createInternationalUSDDeposit(BigDecimal amount, String name, String surname, String comment) throws IOException, BitstampWebdriverException;

	void cancelLastDeposit() throws IOException, BitstampWebdriverException;

}
