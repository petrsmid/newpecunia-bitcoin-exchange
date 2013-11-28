package com.newpecunia.trader.service.impl.processor;

import java.math.BigDecimal;

public interface BitstampAutoTrader {

	void sendUsdToUnicredit(BigDecimal amount);
	
	void sendBtcFromBitstampToWallet(BigDecimal amount);

}
