package com.newpecunia.trader.service;

import java.math.BigDecimal;


public interface TraderService {
	
	BigDecimal getCustomerBtcBuyPriceInUSD();

	void sendBTCsForPayment(String receiverBtcAddress, BigDecimal amount, String email);

}
