package com.newpecunia.bitcoind.service.impl;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.newpecunia.bitcoind.service.BitcoindService;
import com.newpecunia.bitcoind.service.TransactionInfo;
import com.newpecunia.trader.service.TraderService;

@Singleton
public class ReceiveBTCCallback {

	private static final Logger logger = LogManager.getLogger(ReceiveBTCCallback.class);	
	
	private BitcoindService bitcoindService;
	private TraderService traderService;

	@Inject
	ReceiveBTCCallback(BitcoindService bitcoindService, TraderService traderService) {
		this.bitcoindService = bitcoindService;
		this.traderService = traderService;
	}
	
	public void serve(String txId) {
		TransactionInfo info = bitcoindService.getTransactionInfo(txId);
		BigDecimal btcAmount = info.getAmount();
		String receivingBtcAddress = info.getAddress();
		
		logger.info(String.format("Received %s BTC to address %s. Creating payment order for the BTC.", btcAmount.toPlainString(), receivingBtcAddress));
		traderService.payForReceivedBTCs(receivingBtcAddress, btcAmount);
	}
	
}
