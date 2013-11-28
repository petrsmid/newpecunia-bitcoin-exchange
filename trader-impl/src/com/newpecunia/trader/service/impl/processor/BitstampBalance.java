package com.newpecunia.trader.service.impl.processor;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.google.inject.Inject;
import com.newpecunia.NPException;
import com.newpecunia.bitstamp.service.AccountBalance;
import com.newpecunia.bitstamp.service.BitstampService;
import com.newpecunia.bitstamp.service.BitstampServiceException;
import com.newpecunia.trader.service.impl.CachedBuySellPriceCalculator;

public class BitstampBalance {
	
	private BitstampService bitstampService;
	private CachedBuySellPriceCalculator cachedBuySellPriceCalculator;

	@Inject
	BitstampBalance(BitstampService bitstampService, CachedBuySellPriceCalculator cachedBuySellPriceCalculator) {
		this.bitstampService = bitstampService;
		this.cachedBuySellPriceCalculator = cachedBuySellPriceCalculator;
	}
	
	public BigDecimal getBalanceInBTC() {
		AccountBalance balance;
		try {
			balance = bitstampService.getAccountBalance();
		} catch (BitstampServiceException e) {
			throw new NPException("Could not determine balance on Bitstamp", e);
		}
		
		BigDecimal btcs = balance.getBtcAvailable();
		BigDecimal usds = balance.getUsdAvailable();
		
		BigDecimal buyPrice = cachedBuySellPriceCalculator.getBtcBuyPriceInUSD();
		BigDecimal usdsInBtcs = usds.divide(buyPrice, 8, RoundingMode.HALF_UP);
		
		return btcs.add(usdsInBtcs);
	}
	
	public BigDecimal getBalanceInUSD() {
		AccountBalance balance;
		try {
			balance = bitstampService.getAccountBalance();
		} catch (BitstampServiceException e) {
			throw new NPException("Could not determine balance on Bitstamp", e);
		}
		
		BigDecimal btcs = balance.getBtcAvailable();
		BigDecimal usds = balance.getUsdAvailable();
		
		BigDecimal sellPrice = cachedBuySellPriceCalculator.getBtcSellPriceInUSD();
		BigDecimal btcsInUsds = btcs.multiply(sellPrice);
		
		return usds.add(btcsInUsds);
	}
	
	

}
