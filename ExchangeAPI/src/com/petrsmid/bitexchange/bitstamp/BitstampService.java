package com.petrsmid.bitexchange.bitstamp;

import java.math.BigDecimal;
import java.util.List;

import com.petrsmid.bitexchange.bitstamp.impl.dto.AccountBalanceDTO;
import com.petrsmid.bitexchange.bitstamp.impl.dto.EurUsdRateDTO;


public interface BitstampService {

	Ticker getTicker() throws BitstampServiceException;

	OrderBook getOrderBook() throws BitstampServiceException;

	Order buyLimitOrder(BigDecimal price, BigDecimal amount) throws BitstampServiceException;

	Order sellLimitOrder(BigDecimal price, BigDecimal amount) throws BitstampServiceException;

	Boolean cancelOrder(String orderId) throws BitstampServiceException;

	List<Order> getOpenOrders() throws BitstampServiceException;

	EurUsdRateDTO getEurUsdConversionRate() throws BitstampServiceException;

	AccountBalanceDTO getAccountBalance() throws BitstampServiceException;

	Boolean bitcoinWithdrawal(BigDecimal amount, String address) throws BitstampServiceException;

}
