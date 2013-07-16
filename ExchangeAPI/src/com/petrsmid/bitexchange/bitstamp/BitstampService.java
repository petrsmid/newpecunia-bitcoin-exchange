package com.petrsmid.bitexchange.bitstamp;

import java.math.BigDecimal;
import java.util.List;


public interface BitstampService {

	Ticker getTicker() throws BitstampServiceException;

	OrderBook getOrderBook() throws BitstampServiceException;

	Order buyLimitOrder(BigDecimal price, BigDecimal amount) throws BitstampServiceException;

	Order sellLimitOrder(BigDecimal price, BigDecimal amount) throws BitstampServiceException;

	Boolean cancelOrder(String orderId) throws BitstampServiceException;

	List<Order> getOpenOrders() throws BitstampServiceException;

	EurUsdRate getEurUsdConversionRate() throws BitstampServiceException;

	AccountBalance getAccountBalance() throws BitstampServiceException;

	Boolean bitcoinWithdrawal(BigDecimal amount, String address) throws BitstampServiceException;

	String getBitcoinDepositAddress() throws BitstampServiceException;

	List<UserTransaction> getUserTransactions(long limit) throws BitstampServiceException;
	
	List<UserTransaction> getUserTransactions(long offset, long limit) throws BitstampServiceException;

	List<Transaction> getTransactions(long limit) throws BitstampServiceException;

	List<Transaction> getTransactions(long offset, long limit) throws BitstampServiceException;

	List<UnconfirmedBitcoinDeposit> getUnconfirmedBitcoinDeposits() throws BitstampServiceException;

}
