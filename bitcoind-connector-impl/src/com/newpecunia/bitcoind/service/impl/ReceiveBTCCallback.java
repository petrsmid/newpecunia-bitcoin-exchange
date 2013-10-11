package com.newpecunia.bitcoind.service.impl;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.newpecunia.bitcoind.service.BitcoindService;
import com.newpecunia.bitcoind.service.TransactionInfo;
import com.newpecunia.trader.service.TraderService;
import com.newpecunia.unicredit.service.PaymentService;

@Singleton
public class ReceiveBTCCallback {

	private static final Logger logger = LogManager.getLogger(ReceiveBTCCallback.class);	
	
	private BitcoindService bitcoindService;
	private TraderService traderService;
	private PaymentService paymentService;

	@Inject
	ReceiveBTCCallback(BitcoindService bitcoindService, TraderService traderService, PaymentService paymentService) {
		this.bitcoindService = bitcoindService;
		this.traderService = traderService;
		this.paymentService = paymentService;
	}
	
	public void serve(String txId) {
		TransactionInfo info = bitcoindService.getTransactionInfo(txId);
		BigDecimal amount = info.getAmount();
		String receivingBtcAddress = info.getAddress();
		
		BigDecimal amountUsd = traderService.getNPBtcBuyPriceInUSD(amount);
		paymentService.createOrderFromPreOrder(receivingBtcAddress, amountUsd);
		
	}

	public void addReceiveBTCCallback(ReceiveBTCCallback callback) {
		
	}
	
}
