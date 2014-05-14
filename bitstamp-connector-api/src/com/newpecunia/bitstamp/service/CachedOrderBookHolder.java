package com.newpecunia.bitstamp.service;

public interface CachedOrderBookHolder {
	
	OrderBook getOrderBook();
	
	/**
	 * @return age of order book in ms
	 */
	long getAge();

	boolean isOrderBookActual();
}
