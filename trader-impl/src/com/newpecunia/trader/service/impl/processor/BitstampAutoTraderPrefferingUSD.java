package com.newpecunia.trader.service.impl.processor;

import java.math.BigDecimal;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.newpecunia.bitstamp.service.BitstampService;
import com.newpecunia.trader.service.impl.CachedBuySellPriceCalculator;

//TODO reimplement me


@Singleton
public class BitstampAutoTraderPrefferingUSD extends AbstractBitstampAutoTrader {

	@Inject
	BitstampAutoTraderPrefferingUSD(BitstampService bitstampService,
			CachedBuySellPriceCalculator cachedBuySellPriceCalculator) {
		super(bitstampService, cachedBuySellPriceCalculator);
	}

	@Override
	public void sendUsdToUnicredit(BigDecimal amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendBtcFromBitstampToWallet(BigDecimal amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendBtcFromWalletToBitstamp(BigDecimal amount) {
		// TODO Auto-generated method stub
		
	}

}
