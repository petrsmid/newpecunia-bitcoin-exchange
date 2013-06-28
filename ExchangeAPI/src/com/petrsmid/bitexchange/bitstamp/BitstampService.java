package com.petrsmid.bitexchange.bitstamp;

import java.math.BigDecimal;
import java.util.List;


public interface BitstampService {

	Ticker getTicker() throws BitstampServiceException;

	OrderBook getOrderBook() throws BitstampServiceException;

	Order buyLimitOrder(BigDecimal price, BigDecimal amount) throws BitstampServiceException;

	Order sellLimitOrder(BigDecimal price, BigDecimal amount) throws BitstampServiceException;

	boolean cancelOrder(String orderId) throws BitstampServiceException;

	List<Order> getOpenOrders() throws BitstampServiceException;

}
