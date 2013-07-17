package com.newpecunia.bitstamp.service;

import java.math.BigDecimal;

public class PriceAndAmount {
	private BigDecimal amount;
	private BigDecimal price;
	
	
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
}
