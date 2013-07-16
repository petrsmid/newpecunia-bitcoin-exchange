package com.petrsmid.bitexchange.bitstamp;

import java.math.BigDecimal;

public class AccountBalance {

	private BigDecimal usdBalance; // USD balance
	private BigDecimal btcBalance; // BTC balance
	private BigDecimal usdReserved; // USD reserved in open orders
	private BigDecimal btcReserved; // BTC reserved in open orders
	private BigDecimal usdAvailable; // USD available for trading
	private BigDecimal btcAvailable; // BTC available for trading
	private BigDecimal customerTradingFee; // customer trading fee

	
	public BigDecimal getUsdBalance() {
		return usdBalance;
	}
	public void setUsdBalance(BigDecimal usdBalance) {
		this.usdBalance = usdBalance;
	}
	public BigDecimal getBtcBalance() {
		return btcBalance;
	}
	public void setBtcBalance(BigDecimal btcBalance) {
		this.btcBalance = btcBalance;
	}
	public BigDecimal getUsdReserved() {
		return usdReserved;
	}
	public void setUsdReserved(BigDecimal usdReserved) {
		this.usdReserved = usdReserved;
	}
	public BigDecimal getBtcReserved() {
		return btcReserved;
	}
	public void setBtcReserved(BigDecimal btcReserved) {
		this.btcReserved = btcReserved;
	}
	public BigDecimal getUsdAvailable() {
		return usdAvailable;
	}
	public void setUsdAvailable(BigDecimal usdAvailable) {
		this.usdAvailable = usdAvailable;
	}
	public BigDecimal getBtcAvailable() {
		return btcAvailable;
	}
	public void setBtcAvailable(BigDecimal btcAvailable) {
		this.btcAvailable = btcAvailable;
	}
	public BigDecimal getCustomerTradingPercentFee() {
		return customerTradingFee;
	}
	public void setCustomerTradingFee(BigDecimal customerTradingFee) {
		this.customerTradingFee = customerTradingFee;
	}
	
}
