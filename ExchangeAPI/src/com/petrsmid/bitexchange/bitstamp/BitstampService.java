package com.petrsmid.bitexchange.bitstamp;

import java.math.BigDecimal;

import com.petrsmid.bitexchange.StockServiceException;
import com.petrsmid.bitexchange.bitstamp.impl.dto.OrderDTO;
import com.petrsmid.bitexchange.bitstamp.impl.dto.Ticker;

public interface BitstampService {

	Ticker getTicker() throws StockServiceException;

	OrderBook getOrderBook() throws StockServiceException;

	OrderDTO buyLimitOrder(BigDecimal price, BigDecimal amount) throws StockServiceException;

}
