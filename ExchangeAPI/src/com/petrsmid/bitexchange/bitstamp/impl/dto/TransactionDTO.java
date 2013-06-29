package com.petrsmid.bitexchange.bitstamp.impl.dto;

import java.math.BigDecimal;

public class TransactionDTO {

	private String date; // unix timestamp date and time
	private String tid; // transaction id
	private BigDecimal price; // BTC price
	private BigDecimal amount; // BTC amount	
	
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	
}
