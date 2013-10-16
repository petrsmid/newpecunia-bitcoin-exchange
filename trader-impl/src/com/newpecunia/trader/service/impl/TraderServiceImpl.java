package com.newpecunia.trader.service.impl;

import java.math.BigDecimal;

import com.google.inject.Inject;
import com.newpecunia.trader.service.TraderService;
import com.newpecunia.unicredit.service.PaymentService;

public class TraderServiceImpl implements TraderService {

	private PaymentService paymentService;

	@Inject
	TraderServiceImpl(PaymentService paymentService) {
		this.paymentService = paymentService;
	}
	
	@Override
	public BigDecimal getNPBtcBuyPriceInUSD(BigDecimal amountBtc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getNPBtcSellPriceInUSD(BigDecimal amountBtc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void payForReceivedBTCs(String receivingBtcAddress, BigDecimal btcAmount) {
		BigDecimal amountUSD = getNPBtcBuyPriceInUSD(btcAmount);
		paymentService.createOrderFromPreOrder(receivingBtcAddress, amountUSD);
	}

}
