package com.newpecunia.bitcoind.service.impl;

import ru.paradoxs.bitcoin.client.TransactionInfo;

import com.google.inject.Inject;
import com.newpecunia.bitcoind.service.BitcoindService;
import com.newpecunia.bitcoind.service.ReceiveBTCCallback;

public class ReceiveBTCCallbackManager {

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
