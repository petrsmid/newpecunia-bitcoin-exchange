package com.petrsmid.bitexchange.bitstamp;

import com.petrsmid.bitexchange.StockServiceException;

public interface TickerService {
	public Ticker getTicker() throws StockServiceException;
}
