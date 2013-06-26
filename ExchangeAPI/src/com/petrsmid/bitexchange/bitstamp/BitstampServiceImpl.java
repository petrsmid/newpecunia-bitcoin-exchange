package com.petrsmid.bitexchange.bitstamp;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.inject.Inject;
import com.petrsmid.bitexchange.StockServiceException;
import com.petrsmid.bitexchange.bitstamp.dto.OrderBookDTO;
import com.petrsmid.bitexchange.bitstamp.dto.Ticker;
import com.petrsmid.bitexchange.net.HttpReader;

public class BitstampServiceImpl implements BitstampService {

	private HttpReader httpReader;

	@Inject
	public BitstampServiceImpl(HttpReader httpReader) {
		this.httpReader = httpReader;
	}
	
	@Override
	public Ticker getTicker() throws StockServiceException {
		String url = BitstampConstants.TICKER_URL;
		try {
			String output = httpReader.readUrl(url);
			Gson gson = new Gson();
			Ticker ticker = gson.fromJson(output, Ticker.class);
			
			return ticker;
		} catch (IOException | JsonSyntaxException e) {
			throw new StockServiceException(e);
		}
	}

	@Override
	public OrderBook getOrderBook() throws StockServiceException {
		String url = BitstampConstants.ORDER_BOOK_URL;
		try {
			String output = httpReader.readUrl(url);
			Gson gson = new Gson();
			OrderBookDTO orderBookDTO = gson.fromJson(output, OrderBookDTO.class);			
			return mapOrderBookDTO2OrderBook(orderBookDTO);
		} catch (IOException | JsonSyntaxException e) {
			throw new StockServiceException(e);
		}
	}

	private OrderBook mapOrderBookDTO2OrderBook(OrderBookDTO obDTO) {
		OrderBook orderBook = new OrderBook();
		orderBook.setTimestamp(obDTO.getTimestamp());
		orderBook.setAsks(new ArrayList<Order>());
		orderBook.setBids(new ArrayList<Order>());
		
		for (List<BigDecimal> askDTO : obDTO.getAsks()) {
			Order ask = new Order();
			ask.setPrice(askDTO.get(0));
			ask.setAmount(askDTO.get(1));
			orderBook.getAsks().add(ask);
		}

		for (List<BigDecimal> bidDTO : obDTO.getBids()) {
			Order bid = new Order();
			bid.setPrice(bidDTO.get(0));
			bid.setAmount(bidDTO.get(1));
			orderBook.getBids().add(bid);
		}
		
		return orderBook;
	}

}
