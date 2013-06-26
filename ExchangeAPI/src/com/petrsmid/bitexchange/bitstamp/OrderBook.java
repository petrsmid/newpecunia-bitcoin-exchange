package com.petrsmid.bitexchange.bitstamp;

import java.util.List;

public class OrderBook {

	private Long timestamp;
	private List<Order> bids;
	private List<Order> asks;
	
	
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	public List<Order> getBids() {
		return bids;
	}
	public void setBids(List<Order> bids) {
		this.bids = bids;
	}
	public List<Order> getAsks() {
		return asks;
	}
	public void setAsks(List<Order> asks) {
		this.asks = asks;
	}
	
}
