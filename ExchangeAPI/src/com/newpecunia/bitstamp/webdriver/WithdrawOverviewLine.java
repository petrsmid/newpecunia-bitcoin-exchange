package com.newpecunia.bitstamp.webdriver;

import java.math.BigDecimal;

public class WithdrawOverviewLine {

	public static enum WithdrawType { INTERNATIONAL_BANK_TRANSFER, BITCOIN }
	public static enum WithdrawStatus { FINISHED, CANCELED, WAITING_FOR_CONFIRMATION, WAITING_FOR_BTC_PROCESSING }
	
	private long id;
	private String date;
	private String description;
	private BigDecimal amount;
	private WithdrawStatus status;
	private WithdrawType withdrawType;
	private String cancelUrl;

	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
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
	public WithdrawStatus getStatus() {
		return status;
	}
	public void setStatus(WithdrawStatus status) {
		this.status = status;
	}
	public WithdrawType getWithdrawType() {
		return withdrawType;
	}
	public void setWithdrawType(WithdrawType withdrawType) {
		this.withdrawType = withdrawType;
	}
	public String getCancelUrl() {
		return cancelUrl;
	}
	public void setCancelUrl(String cancelUrl) {
		this.cancelUrl = cancelUrl;
	}
	
}
