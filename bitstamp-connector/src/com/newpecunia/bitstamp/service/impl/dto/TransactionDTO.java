package com.newpecunia.bitstamp.service.impl.dto;

import java.math.BigDecimal;

public class TransactionDTO {

	private Long date; // unix timestamp date and time
	private String tid; // transaction id
	private BigDecimal price; // BTC price
	private BigDecimal amount; // BTC amount	
	
	
	public Long getDate() {
		return date;
	}
	public void setDate(Long date) {
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
