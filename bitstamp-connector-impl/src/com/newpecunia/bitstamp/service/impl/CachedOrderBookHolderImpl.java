package com.newpecunia.bitstamp.service.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.newpecunia.bitstamp.service.CachedOrderBookHolder;
import com.newpecunia.bitstamp.service.OrderBook;
import com.newpecunia.configuration.NPConfiguration;
import com.newpecunia.time.TimeProvider;

@Singleton
public class CachedOrderBookHolderImpl implements CachedOrderBookHolder {

	private volatile OrderBook orderBook = null; //TODO replicate to all cluster nodes
	
	private volatile long lastUpdate = 0;

	private TimeProvider timeProvider;
	private NPConfiguration config;
	
	
	@Inject
	CachedOrderBookHolderImpl(TimeProvider timeProvider, NPConfiguration configuration) {
		this.timeProvider = timeProvider;
		this.config = configuration;
	}
	
	
	@Override
	public OrderBook getOrderBook() {
		return orderBook;
	}
	
	public void setOrderBook(OrderBook orderBook) {
		lastUpdate = timeProvider.now();
		this.orderBook = orderBook;
	}

	@Override
	public long getAge() {
		return timeProvider.now() - lastUpdate;
	}
	
	@Override
	public boolean isOrderBookActual() {
		return getAge() < config.getMaxOrderbookAgeInMsToBeConsideredActual();
	}

}
