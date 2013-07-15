package com.petrsmid.bitexchange.bitstamp;

import java.math.BigDecimal;

import org.joda.time.DateTime;

public class Transaction {

	private DateTime timestamp; // unix timestamp date and time
	private String transactionId; // transaction id
	private BigDecimal btcPrice; // BTC price
	private BigDecimal btcAmount; // BTC amount

	
	public DateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(DateTime timestamp) {
		this.timestamp = timestamp;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public BigDecimal getBtcPrice() {
		return btcPrice;
	}
	public void setBtcPrice(BigDecimal btcPrice) {
		this.btcPrice = btcPrice;
	}
	public BigDecimal getBtcAmount() {
		return btcAmount;
	}
	public void setBtcAmount(BigDecimal btcAmount) {
		this.btcAmount = btcAmount;
	}
	
	
}
