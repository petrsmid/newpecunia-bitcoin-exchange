package com.newpecunia.trader.service.impl;

import java.math.BigDecimal;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.newpecunia.time.TimeProvider;
import com.newpecunia.trader.service.PriceService;

@Singleton
public class PriceServiceImpl implements PriceService {

	private static final Logger logger = LogManager.getLogger(PriceServiceImpl.class);	

	private CachedBuySellPriceCalculator cachedBuySellPriceCalc;
	private TimeProvider timeProvider;
	private Provider<EntityManager> emProvider;

	
	@Inject
	PriceServiceImpl(
			CachedBuySellPriceCalculator cachedBuySellPriceCalc,
			TimeProvider timeProvider,
			Provider<EntityManager> emProvider) {
		
		this.cachedBuySellPriceCalc = cachedBuySellPriceCalc;
		this.timeProvider = timeProvider;
		this.emProvider = emProvider;
	}

	@Override
	public BigDecimal getCustomerBtcBuyPriceInUSD() {
		//TODO add interest in some %
		return cachedBuySellPriceCalc.getBtcBuyPriceInUSD();
	}

}
