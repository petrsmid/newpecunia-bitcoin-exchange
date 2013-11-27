package com.newpecunia.unicredit.webdav.impl;

import java.math.BigDecimal;
import java.util.List;

public class Statement {
	private BigDecimal balance;
	private List<String> foundNonBitstampPaymentReferences;
	private int bitstampReferencesCount = 0;
	
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public List<String> getFoundNonBitstampPaymentReferences() {
		return foundNonBitstampPaymentReferences;
	}
	public void setFoundNonBitstampPaymentReferences(List<String> foundNonBitstampPaymentReferences) {
		this.foundNonBitstampPaymentReferences = foundNonBitstampPaymentReferences;
	}
	public int getBitstampReferencesCount() {
		return bitstampReferencesCount;
	}
	public void setBitstampReferencesCount(int bitstampReferencesCount) {
		this.bitstampReferencesCount = bitstampReferencesCount;
	}
	
	

}
