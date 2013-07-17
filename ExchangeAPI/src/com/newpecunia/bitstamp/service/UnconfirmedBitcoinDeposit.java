package com.newpecunia.bitstamp.service;

import java.math.BigDecimal;

public class UnconfirmedBitcoinDeposit {

	private BigDecimal amount; // bitcoin amount
	private String address; // deposit address used
	private int confirmations; // number of confirmations	
	
	
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getConfirmations() {
		return confirmations;
	}
	public void setConfirmations(int confirmations) {
		this.confirmations = confirmations;
	}
	
}
