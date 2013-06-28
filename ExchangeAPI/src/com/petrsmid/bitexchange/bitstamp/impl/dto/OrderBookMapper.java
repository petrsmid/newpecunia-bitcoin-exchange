package com.petrsmid.bitexchange.bitstamp.impl.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.petrsmid.bitexchange.bitstamp.OrderBook;
import com.petrsmid.bitexchange.bitstamp.PriceAndAmount;

public class OrderBookMapper {

	public static OrderBook mapOrderBookDTO2OrderBook(OrderBookDTO obDTO) {
		OrderBook orderBook = new OrderBook();
		orderBook.setTimestamp(obDTO.getTimestamp());
		orderBook.setAsks(new ArrayList<PriceAndAmount>());
		orderBook.setBids(new ArrayList<PriceAndAmount>());
		
		for (List<BigDecimal> askDTO : obDTO.getAsks()) {
			PriceAndAmount ask = new PriceAndAmount();
			ask.setPrice(askDTO.get(0));
			ask.setAmount(askDTO.get(1));
			orderBook.getAsks().add(ask);
		}

		for (List<BigDecimal> bidDTO : obDTO.getBids()) {
			PriceAndAmount bid = new PriceAndAmount();
			bid.setPrice(bidDTO.get(0));
			bid.setAmount(bidDTO.get(1));
			orderBook.getBids().add(bid);
		}
		
		return orderBook;
	}
}
