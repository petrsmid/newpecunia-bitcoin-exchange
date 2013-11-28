package com.newpecunia.trader.service.impl.processor;

import java.math.BigDecimal;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.newpecunia.bitstamp.service.BitstampService;
import com.newpecunia.trader.service.impl.CachedBuySellPriceCalculator;

//TODO reimplement me


@Singleton
public class BitstampAutoTraderPrefferingUSD implements BitstampAutoTrader {

	@Inject
	BitstampAutoTraderPrefferingUSD() {
	}

	@Override
	public void sendUsdToUnicredit(BigDecimal amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendBtcFromBitstampToWallet(BigDecimal amount) {
		// TODO Auto-generated method stub
		
	}

}
