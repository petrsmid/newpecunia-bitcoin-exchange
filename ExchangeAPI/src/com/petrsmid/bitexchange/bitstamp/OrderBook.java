package com.petrsmid.bitexchange.bitstamp;

import java.util.List;

public class OrderBook {

	private Long timestamp;
	private List<PriceAndAmount> bids;
	private List<PriceAndAmount> asks;
	
	
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	public List<PriceAndAmount> getBids() {
		return bids;
	}
	public void setBids(List<PriceAndAmount> bids) {
		this.bids = bids;
	}
	public List<PriceAndAmount> getAsks() {
		return asks;
	}
	public void setAsks(List<PriceAndAmount> asks) {
		this.asks = asks;
	}
	
}
