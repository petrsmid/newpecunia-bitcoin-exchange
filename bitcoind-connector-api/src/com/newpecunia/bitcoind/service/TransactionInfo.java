package com.newpecunia.bitcoind.service;

import java.math.BigDecimal;

public class TransactionInfo {
	public enum Category { GENERATE, SEND, RECEIVE, MOVE }
	
    private Category category;     // Can be null, "generate", "send", "receive", or "move"
    private BigDecimal amount;   // Can be positive or negative
    private BigDecimal fee;      // Only for send, can be 0.0
    private long confirmations;  // only for generate/send/receive
    private String txId;         // only for generate/send/receive
    private String comment;
    private String address;
    
    
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getFee() {
		return fee;
	}
	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
	public long getConfirmations() {
		return confirmations;
	}
	public void setConfirmations(long confirmations) {
		this.confirmations = confirmations;
	}
	public String getTxId() {
		return txId;
	}
	public void setTxId(String txId) {
		this.txId = txId;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
    
    
}
