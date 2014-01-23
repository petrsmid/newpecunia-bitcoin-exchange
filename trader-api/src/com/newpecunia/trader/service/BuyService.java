package com.newpecunia.trader.service;

import java.math.BigDecimal;

public interface BuyService {
	
	void createPreOrder(BigDecimal btcAmount, BigDecimal usdAmount, String btcAddress, String email, String cardTransactionId);

}
