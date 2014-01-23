package com.newpecunia.trader.service;

import java.math.BigDecimal;


public interface TraderService {
	
	BigDecimal getCustomerBtcBuyPriceInUSD();
	BigDecimal getCustomerBtcSellPriceInUSD();
	BigDecimal getCustomerBtcBuyPriceInUSD(BigDecimal amountBtc);
	BigDecimal getCustomerBtcSellPriceInUSD(BigDecimal amountBtc);
	void payForReceivedBTCs(String receivingBtcAddress, BigDecimal btcAmount);
	void sendBTCsForPayment(String receiverBtcAddress, BigDecimal amount, String email);

}
