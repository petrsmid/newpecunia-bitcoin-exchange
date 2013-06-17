package com.petrsmid.bitexchange.bitstamp;

import com.petrsmid.bitexchange.StockServiceException;

public interface BitstampService {

	Ticker getTicker() throws StockServiceException;

	OrderBook getOrderBook();

}
