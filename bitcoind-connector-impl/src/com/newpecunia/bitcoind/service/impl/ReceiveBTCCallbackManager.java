package com.newpecunia.bitcoind.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;
import com.newpecunia.bitcoind.service.BitcoindService;
import com.newpecunia.bitcoind.service.ReceiveBTCCallback;
import com.newpecunia.bitcoind.service.TransactionInfo;

public class ReceiveBTCCallbackManager {

	private static final Logger logger = LogManager.getLogger(ReceiveBTCCallbackManager.class);	
	
	private BitcoindService bitcoindService;

	@Inject
	ReceiveBTCCallbackManager(BitcoindService bitcoindService) {
		this.bitcoindService = bitcoindService;
	}
	
	public void serve(String txId) {
		TransactionInfo info = bitcoindService.getTransactionInfo(txId);
		info.getAmount();
		info.getAddress();
		//TODO - find corresponding callback and perform it
	}

	public void addReceiveBTCCallback(ReceiveBTCCallback callback) {
		
	}
	
	public void removeReceiveBTCCallback(ReceiveBTCCallback callback) {
		
	}
	
	
}
