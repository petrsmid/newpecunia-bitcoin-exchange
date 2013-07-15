package com.petrsmid.bitexchange.bitstamp;

import java.math.BigDecimal;

import org.joda.time.DateTime;

public class UserTransaction {

	public enum TransactionType { DEPOSIT, WITHDRAWAL, TRADE }
	
	private DateTime datetime; // date and time
	private String transactionId; // transaction id
	private TransactionType type; // transaction type (0 - deposit; 1 - withdrawal; 2 - market trade)
	private BigDecimal amountUSD; // USD amount
	private BigDecimal amountBTC; // BTC amount
	private BigDecimal transactionFee; // transaction fee
	private BigDecimal orderId; // executed order id	

	
	public DateTime getDatetime() {
		return datetime;
	}
	public void setDatetime(DateTime datetime) {
		this.datetime = datetime;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public TransactionType getType() {
		return type;
	}
	public void setType(TransactionType type) {
		this.type = type;
	}
	public BigDecimal getAmountUSD() {
		return amountUSD;
	}
	public void setAmountUSD(BigDecimal amountUSD) {
		this.amountUSD = amountUSD;
	}
	public BigDecimal getAmountBTC() {
		return amountBTC;
	}
	public void setAmountBTC(BigDecimal amountBTC) {
		this.amountBTC = amountBTC;
	}
	public BigDecimal getTransactionFee() {
		return transactionFee;
	}
	public void setTransactionFee(BigDecimal transactionFee) {
		this.transactionFee = transactionFee;
	}
	public BigDecimal getOrderId() {
		return orderId;
	}
	public void setOrderId(BigDecimal orderId) {
		this.orderId = orderId;
	}
	
}
