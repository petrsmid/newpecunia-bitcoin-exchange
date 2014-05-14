package com.newpecunia.trader.service.impl;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.newpecunia.bitstamp.service.CachedOrderBookHolder;
import com.newpecunia.configuration.NPConfiguration;
import com.newpecunia.trader.service.PriceService;

@Singleton
public class PriceServiceImpl implements PriceService {

	private static final Logger logger = LogManager.getLogger(PriceServiceImpl.class);	

	private NPConfiguration configuration;

	private CachedOrderBookHolder orderBookHolder;

	
	@Inject
	PriceServiceImpl(
			CachedOrderBookHolder orderBookHolder,
			NPConfiguration configuration) {
		
		this.configuration = configuration;
		this.orderBookHolder = orderBookHolder;
	}

	@Override
	public BigDecimal getCustomerBtcBuyPriceInUSD() {
		BigDecimal interest = configuration.getInterestInPercent();
		BigDecimal marketPrice = this.getMarketBtcBuyPriceInUSD();
		if (marketPrice == null) {return null;}
		
		BigDecimal multiplier = (new BigDecimal(100)).add(interest).multiply(new BigDecimal("0.01"));
		
		return marketPrice.multiply(multiplier);
	}

	@Override
	public BigDecimal getMarketBtcBuyPriceInUSD() {
		if (orderBookHolder.isOrderBookActual()) {
			BigDecimal avgAsk = AvgPriceCalculator.calculateAvgBtcPriceForBTCs(orderBookHolder.getOrderBook().getAsks(), 
					configuration.getNbrOfBtcsForPriceCalculation() /*TODO instead of a hardcoded value consider volume of sales*/);
			return avgAsk;
		} else {
			return null;
		}
	}

}
