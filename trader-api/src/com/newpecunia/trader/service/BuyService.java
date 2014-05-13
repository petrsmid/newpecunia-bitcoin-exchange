package com.newpecunia.trader.service;

import java.math.BigDecimal;

import com.newpecunia.countries.Country;

public interface BuyService {
	
	String createUnconfirmedPreorder(BigDecimal btcAmount, String btcAddress, String email, String name, String address, String city, Country country);
	
	UnconfirmedBuyPreOrder getUnconfirmedPreorder(String id);
	
	void createPreOrder(BigDecimal btcAmount, BigDecimal usdAmount, String btcAddress, String email, String cardTransactionId, String name, String city, String countryCode, String txId);

}
