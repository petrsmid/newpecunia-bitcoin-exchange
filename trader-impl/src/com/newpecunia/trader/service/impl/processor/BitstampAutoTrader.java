package com.newpecunia.trader.service.impl.processor;

import java.math.BigDecimal;

//TODO - reimplement me - the settle up jobs perform already sending from/to Unicredit and from/to Bitcoin wallet 

public interface BitstampAutoTrader {

	BigDecimal getBalanceInBTC();

	BigDecimal getBalanceInUSD();

	void sendUsdToUnicredit(BigDecimal amount);
	
	void sendBtcFromBitstampToWallet(BigDecimal amount);

	void sendBtcFromWalletToBitstamp(BigDecimal amount);
	
}
