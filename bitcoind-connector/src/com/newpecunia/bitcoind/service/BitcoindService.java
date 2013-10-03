package com.newpecunia.bitcoind.service;

import java.math.BigDecimal;

import ru.paradoxs.bitcoin.client.TransactionInfo;

public interface BitcoindService {
	
	BigDecimal getBalance();
	
	void sendMoney(String address, BigDecimal amount, String comment, String commentTo);

	TransactionInfo getTransactionInfo(String txId);


}
