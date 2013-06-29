package com.petrsmid.bitexchange.bitstamp.impl.dto;

import java.math.BigDecimal;

public class AccountBalanceDTO {

	private BigDecimal usd_balance; // USD balance
	private BigDecimal btc_balance; // BTC balance
	private BigDecimal usd_reserved; // USD reserved in open orders
	private BigDecimal btc_reserved; // BTC reserved in open orders
	private BigDecimal usd_available; // USD available for trading
	private BigDecimal btc_available; // BTC available for trading
	private BigDecimal fee; // customer trading fee	

	
	
	public BigDecimal getUsd_balance() {
		return usd_balance;
	}
	public void setUsd_balance(BigDecimal usd_balance) {
		this.usd_balance = usd_balance;
	}
	public BigDecimal getBtc_balance() {
		return btc_balance;
	}
	public void setBtc_balance(BigDecimal btc_balance) {
		this.btc_balance = btc_balance;
	}
	public BigDecimal getUsd_reserved() {
		return usd_reserved;
	}
	public void setUsd_reserved(BigDecimal usd_reserved) {
		this.usd_reserved = usd_reserved;
	}
	public BigDecimal getBtc_reserved() {
		return btc_reserved;
	}
	public void setBtc_reserved(BigDecimal btc_reserved) {
		this.btc_reserved = btc_reserved;
	}
	public BigDecimal getUsd_available() {
		return usd_available;
	}
	public void setUsd_available(BigDecimal usd_available) {
		this.usd_available = usd_available;
	}
	public BigDecimal getBtc_available() {
		return btc_available;
	}
	public void setBtc_available(BigDecimal btc_available) {
		this.btc_available = btc_available;
	}
	public BigDecimal getFee() {
		return fee;
	}
	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
	
	
	
}
