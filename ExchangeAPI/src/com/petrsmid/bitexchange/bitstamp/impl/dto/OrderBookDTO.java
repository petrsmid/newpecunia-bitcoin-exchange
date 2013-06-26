package com.petrsmid.bitexchange.bitstamp.impl.dto;

import java.math.BigDecimal;
import java.util.List;

public class OrderBookDTO {
	private Long timestamp;
	private List<List<BigDecimal>> bids;
	private List<List<BigDecimal>> asks;
	
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	public List<List<BigDecimal>> getBids() {
		return bids;
	}
	public void setBids(List<List<BigDecimal>> bids) {
		this.bids = bids;
	}
	public List<List<BigDecimal>> getAsks() {
		return asks;
	}
	public void setAsks(List<List<BigDecimal>> asks) {
		this.asks = asks;
	}
}
