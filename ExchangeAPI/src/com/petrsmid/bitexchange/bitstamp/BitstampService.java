package com.petrsmid.bitexchange.bitstamp;

import com.petrsmid.bitexchange.StockServiceException;
import com.petrsmid.bitexchange.bitstamp.dto.Ticker;

public interface BitstampService {

	Ticker getTicker() throws StockServiceException;

	OrderBook getOrderBook() throws StockServiceException;

}
