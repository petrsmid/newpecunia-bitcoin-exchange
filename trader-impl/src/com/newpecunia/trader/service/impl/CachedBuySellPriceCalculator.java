package com.newpecunia.trader.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.locks.Lock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.newpecunia.NPException;
import com.newpecunia.bitstamp.service.BitstampService;
import com.newpecunia.bitstamp.service.BitstampServiceException;
import com.newpecunia.bitstamp.service.OrderBook;
import com.newpecunia.bitstamp.service.PriceAndAmount;
import com.newpecunia.configuration.NPConfiguration;
import com.newpecunia.synchronization.LockProvider;
import com.newpecunia.time.TimeProvider;

@Singleton
public class CachedBuySellPriceCalculator {
	
	private static final Logger logger = LogManager.getLogger(CachedBuySellPriceCalculator.class);	
	
	private BitstampService bitstampService;
	private NPConfiguration configuration;
	private TimeProvider timeProvider;

	private Lock lock;  //!!! redistribute in clustered environment
	private long lastActualisation = 0l; //!!! redistribute in clustered environment
	private BigDecimal lastBuyPrice = null; //!!! redistribute in clustered environment
	private BigDecimal lastSellPrice = null; //!!! redistribute in clustered environment

	@Inject
	CachedBuySellPriceCalculator(BitstampService bitstampService, NPConfiguration configuration, TimeProvider timeProvider, LockProvider locker) {
		this.bitstampService = bitstampService;
		this.configuration = configuration;
		this.timeProvider = timeProvider;
		this.lock = locker.getLock();
	}

	private void actualiseOrderBookAndPrices() {
		OrderBook orderBook;
		try {
			orderBook = bitstampService.getOrderBook();
		} catch (BitstampServiceException e) {
			throw new NPException("Could not actualise order book of Bitstamp.", e);
		}
		BigDecimal avgAsk = calculateAvgPrice(orderBook.getAsks(), configuration.getNbrOfBtcsForPriceCalculation() /*TODO instead of a hardcoded value consider volume of sales*/);
		BigDecimal avgBid = calculateAvgPrice(orderBook.getBids(), configuration.getNbrOfBtcsForPriceCalculation() /*TODO instead of a hardcoded value consider volume of sales*/);
		
		lastBuyPrice = avgAsk;
		lastSellPrice = avgBid;
	}
	
	private void actualiseOrderBookIfNeeded() {
		try {
			lock.lock();
			long now = timeProvider.now();
			if ((now - lastActualisation) >= configuration.getBtcPriceActualisationPeriodInSecs()*1000) {
				actualiseOrderBookAndPrices();
				lastActualisation = now;
			}
		} finally {
			lock.unlock();
		}
	}
	
	private BigDecimal calculateAvgPrice(List<PriceAndAmount> orders, int forMaxBtcs) {
		BigDecimal sumAmount = BigDecimal.ZERO;
		BigDecimal sumPrice = BigDecimal.ZERO;
		int i = 0;
		for (PriceAndAmount order : orders) {
			logger.debug("Considering order number "+ ++i +" for price");
			sumAmount = sumAmount.add(order.getAmount());
			sumPrice = sumPrice.add(order.getPrice().multiply(order.getAmount()));
			if (sumAmount.compareTo(new BigDecimal(forMaxBtcs)) >= 0) {
				break;
			}
		}
		BigDecimal avgPrice = sumPrice.divide(sumAmount, 2, RoundingMode.HALF_UP);
		return avgPrice;
	}
	
	public BigDecimal getBtcBuyPriceInUSD() {
		actualiseOrderBookIfNeeded();
		return lastBuyPrice;
	}


	public BigDecimal getBtcSellPriceInUSD() {
		actualiseOrderBookIfNeeded();
		return lastSellPrice;
	}
	
	
	
	

}

