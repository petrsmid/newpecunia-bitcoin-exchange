package com.petrsmid.bitexchange.bitstamp.impl.dto;

import java.math.BigDecimal;

public class UserTransactionDTO {

	private String datetime; // date and time
	private String id; // transaction id
	private String type; // transaction type (0 - deposit; 1 - withdrawal; 2 - market trade)
	private BigDecimal usd; // USD amount
	private BigDecimal btc; // BTC amount
	private BigDecimal fee; // transaction fee
	private BigDecimal oder_id; // executed order id

	
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
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
	public BigDecimal getFee() {
		return fee;
	}
	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
	public BigDecimal getOder_id() {
		return oder_id;
	}
	public void setOder_id(BigDecimal oder_id) {
		this.oder_id = oder_id;
	}
	
	
}
