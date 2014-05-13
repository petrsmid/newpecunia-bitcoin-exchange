package com.newpecunia.trader.service;

import java.math.BigDecimal;
import java.util.Calendar;

public class UnconfirmedBuyPreOrder {

	private String id;
	private BigDecimal btcAmount;
	private String btcAddress;
	private String email;
	private String name;
	private String address;
	private String city;
	private String countryCode;
	private Calendar createTimestamp;

	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public BigDecimal getBtcAmount() {
		return btcAmount;
	}
	public void setBtcAmount(BigDecimal btcAmount) {
		this.btcAmount = btcAmount;
	}
	public String getBtcAddress() {
		return btcAddress;
	}
	public void setBtcAddress(String btcAddress) {
		this.btcAddress = btcAddress;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public Calendar getCreateTimestamp() {
		return createTimestamp;
	}
	public void setCreateTimestamp(Calendar createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

}
