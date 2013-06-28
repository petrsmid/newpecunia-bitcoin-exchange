package com.petrsmid.bitexchange.bitstamp;

import java.math.BigDecimal;
import java.util.List;

import com.petrsmid.bitexchange.StockServiceException;

public interface BitstampService {

	Ticker getTicker() throws StockServiceException;

	OrderBook getOrderBook() throws StockServiceException;

	Order buyLimitOrder(BigDecimal price, BigDecimal amount) throws StockServiceException;

	Order sellLimitOrder(BigDecimal price, BigDecimal amount) throws StockServiceException;

	boolean cancelOrder(String orderId) throws StockServiceException;

	List<Order> getOpenOrders() throws StockServiceException;

}
