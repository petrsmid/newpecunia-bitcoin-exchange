package com.newpecunia.unicredit.webdav.impl;

import java.math.BigDecimal;
import java.util.List;

public class Statement {
	private BigDecimal balance;
	private List<String> foundPaymentReferences;
	
	
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public List<String> getFoundPaymentReferences() {
		return foundPaymentReferences;
	}
	public void setFoundPaymentReferences(List<String> foundPaymentReferences) {
		this.foundPaymentReferences = foundPaymentReferences;
	}

}
