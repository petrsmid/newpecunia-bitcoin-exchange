package com.petrsmid.bitexchange.bitstamp;

import java.math.BigDecimal;

public class Order {
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
