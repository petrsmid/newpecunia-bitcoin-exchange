package com.newpecunia.bitstamp.service;

import java.math.BigDecimal;

import org.joda.time.DateTime;

public class Order {
	public enum OrderType {BUY, SELL}
	
	private BigDecimal amount;
	private BigDecimal price;
	private String id;
	private DateTime datetime;
	private OrderType orderType;

	
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public DateTime getDatetime() {
		return datetime;
	}
	public void setDatetime(DateTime datetime) {
		this.datetime = datetime;
	}
	public OrderType getOrderType() {
		return orderType;
	}
	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}
	
	
}
