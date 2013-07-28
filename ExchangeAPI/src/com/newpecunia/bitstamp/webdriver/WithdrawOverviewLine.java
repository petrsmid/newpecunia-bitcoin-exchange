package com.newpecunia.bitstamp.webdriver;

import java.math.BigDecimal;

import org.joda.time.DateTime;

public class WithdrawOverviewLine {

	private long id;
	private DateTime date;
	private String description;
	private BigDecimal amount;
	private String status;

	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public DateTime getDate() {
		return date;
	}
	public void setDate(DateTime date) {
		this.date = date;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
