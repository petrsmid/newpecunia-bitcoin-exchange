package com.petrsmid.bitexchange.bitstamp.impl.dto;

import com.petrsmid.bitexchange.bitstamp.PriceAndAmount;

public class OrderDTO extends PriceAndAmount {
	private String id;
	private String datetime;
	private int type;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}

}
