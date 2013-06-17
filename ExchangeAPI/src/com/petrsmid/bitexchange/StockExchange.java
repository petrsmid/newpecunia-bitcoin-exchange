package com.petrsmid.bitexchange;

import java.math.BigDecimal;

public interface StockExchange {
	/**
	 * @param amountOfBtc - the price of 1 BTC can differ according to the amount what one want to buy
	 * @return price of 1 BTC in USD when buying BTC
	 * @throws StockServiceException 
	 */
	BigDecimal getBuyBtcPrice_USD(BigDecimal amountOfBtc) throws StockServiceException;
	
	/**
	 * @param amountOfBtc - the price of 1 BTC can differ according to the amount what one want to sell
	 * @return price of 1 BTC in USD when selling BTC
	 */
	BigDecimal getSellBtcPrice_USD(BigDecimal amountOfBtc);
	
	/**
	 * @return how many USD has the stock exchange actually available - it is not possible to withdraw more.
	 * However even the withdraw should be significantly smaller.
	 */
	BigDecimal getStockReserve_USD();
	
	/**
	 * @return what is the actual balance of our BTCs in the stock exchange 
	 */
	BigDecimal getBtcAvailabeForTrading();
	
	
	/**
	 * @return what is the actual balance of our USD in the stock exchange 
	 */
	BigDecimal getMoneyAvailableForTrading_USD();
	
	/**
	 * @param amount defines how many BTCs should be send
	 * @param address where to send the BTCs
	 * @return true when successful
	 */
	boolean withdrawBTC(BigDecimal amount, String address);
	
	/**
	 * @param amount defines how many BTC should be withdrawed to the bank address which is defined in the stocek exchange profile
	 * @return
	 */
	boolean withdrawUSD(BigDecimal amount);
}
