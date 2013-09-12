package com.newpecunia.bitstamp.service.impl.dto;

import java.math.BigDecimal;

public class UserTransactionDTO {

	private String datetime; // date and time
	private String id; // transaction id
	private Integer type; // transaction type (0 - deposit; 1 - withdrawal; 2 - market trade)
	private BigDecimal usd; // USD amount
	private BigDecimal btc; // BTC amount
	private BigDecimal btc_usd; //USDs for 1 BTC
	private BigDecimal fee; // transaction fee
	private BigDecimal order_id; // executed order id

	
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public BigDecimal getUsd() {
		return usd;
	}
	public void setUsd(BigDecimal usd) {
		this.usd = usd;
	}
	public BigDecimal getBtc() {
		return btc;
	}
	public void setBtc(BigDecimal btc) {
		this.btc = btc;
	}
	public BigDecimal getBtc_usd() {
		return btc_usd;
	}
	public void setBtc_usd(BigDecimal btc_usd) {
		this.btc_usd = btc_usd;
	}
	public BigDecimal getFee() {
		return fee;
	}
	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
	public BigDecimal getOrder_id() {
		return order_id;
	}
	public void setOrder_id(BigDecimal order_id) {
		this.order_id = order_id;
	}
	
}
