package com.petrsmid.bitexchange.bitstamp;

import java.math.BigDecimal;
import java.util.List;

import com.petrsmid.bitexchange.StockServiceException;
import com.petrsmid.bitexchange.bitstamp.impl.dto.OrderDTO;
import com.petrsmid.bitexchange.bitstamp.impl.dto.Ticker;

public interface BitstampService {

	Ticker getTicker() throws StockServiceException;

	OrderBook getOrderBook() throws StockServiceException;

	OrderDTO buyLimitOrder(BigDecimal price, BigDecimal amount) throws StockServiceException;

	OrderDTO sellLimitOrder(BigDecimal price, BigDecimal amount) throws StockServiceException;

	boolean cancelOrder(String orderId) throws StockServiceException;

	List<OrderDTO> getOpenOrders() throws StockServiceException;

}
