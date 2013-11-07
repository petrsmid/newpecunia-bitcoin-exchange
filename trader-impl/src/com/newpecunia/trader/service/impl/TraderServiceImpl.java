package com.newpecunia.trader.service.impl;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.newpecunia.bitcoind.service.BitcoindService;
import com.newpecunia.trader.service.TraderService;
import com.newpecunia.unicredit.service.PaymentService;

@Singleton
public class TraderServiceImpl implements TraderService {

	private static final Logger logger = LogManager.getLogger(TraderServiceImpl.class);	
	
	private BitcoindService bitcoindService;
	private PaymentService paymentService;
	private CachedBuySellPriceCalculator cachedBuySellPriceCalc;
	
	@Inject
	TraderServiceImpl(BitcoindService bitcoindService, PaymentService paymentService, CachedBuySellPriceCalculator cachedBuySellPriceCalc) {
		this.bitcoindService = bitcoindService;
		this.paymentService = paymentService;
		this.cachedBuySellPriceCalc = cachedBuySellPriceCalc;
	}
	
	@Override
	public BigDecimal getNPBtcBuyPriceInUSD(BigDecimal amountBtc) {
		return cachedBuySellPriceCalc.getBtcBuyPriceInUSD();
	}

	@Override
	public BigDecimal getNPBtcSellPriceInUSD(BigDecimal amountBtc) {
		return cachedBuySellPriceCalc.getBtcSellPriceInUSD();
	}

	@Override
	public void payForReceivedBTCs(String receivingBtcAddress, BigDecimal btcAmount) {
		BigDecimal amountUSD = getNPBtcBuyPriceInUSD(btcAmount);
		paymentService.createOrderFromPreOrder(receivingBtcAddress, amountUSD);
	}
	
	@Override
	public void sendBTCsForPayment(String receiverBtcAddress, BigDecimal amount) {
		bitcoindService.sendMoney(receiverBtcAddress, amount, "" /*comment for sender*/, "New Pecunia" /*comment for receiver*/);
	}

}
