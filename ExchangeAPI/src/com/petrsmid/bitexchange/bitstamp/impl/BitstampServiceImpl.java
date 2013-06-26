package com.petrsmid.bitexchange.bitstamp.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.petrsmid.bitexchange.StockServiceException;
import com.petrsmid.bitexchange.bitstamp.BitstampService;
import com.petrsmid.bitexchange.bitstamp.OrderBook;
import com.petrsmid.bitexchange.bitstamp.PriceAndAmount;
import com.petrsmid.bitexchange.bitstamp.impl.dto.OrderBookDTO;
import com.petrsmid.bitexchange.bitstamp.impl.dto.OrderDTO;
import com.petrsmid.bitexchange.bitstamp.impl.dto.OrderRequest;
import com.petrsmid.bitexchange.bitstamp.impl.dto.Ticker;
import com.petrsmid.bitexchange.net.HttpReader;
import com.petrsmid.bitexchange.net.JsonCodec;
import com.petrsmid.bitexchange.net.JsonParsingException;

public class BitstampServiceImpl implements BitstampService {

	private HttpReader httpReader;
	private BitstampCredentials credentials;
	
	@Inject
	public BitstampServiceImpl(HttpReader httpReader, BitstampCredentials credentials) {
		this.httpReader = httpReader;
		this.credentials = credentials;
	}
	
	@Override
	public Ticker getTicker() throws StockServiceException {
		String url = BitstampConstants.TICKER_URL;
		try {
			String output = httpReader.get(url);
			Ticker ticker = JsonCodec.INSTANCE.parseJson(output, Ticker.class);
			return ticker;
		} catch (IOException | JsonParsingException e) {
			throw new StockServiceException(e);
		}
	}

	@Override
	public OrderBook getOrderBook() throws StockServiceException {
		String url = BitstampConstants.ORDER_BOOK_URL;
		try {
			String output = httpReader.get(url);
			OrderBookDTO orderBookDTO = JsonCodec.INSTANCE.parseJson(output, OrderBookDTO.class);
			return mapOrderBookDTO2OrderBook(orderBookDTO);
		} catch (IOException | JsonParsingException e) {
			throw new StockServiceException(e);
		}
	}

	private OrderBook mapOrderBookDTO2OrderBook(OrderBookDTO obDTO) {
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
	

	@Override
	public OrderDTO buyLimitOrder(BigDecimal price, BigDecimal amount) throws StockServiceException {
		OrderRequest request = new OrderRequest();
		request.setUser(credentials.getUsername());
		request.setPassword(credentials.getPassword());
		request.setPrice(price);
		request.setAmount(amount);
		
		String url = BitstampConstants.BUY_LIMIT_ORDER;
		try {
			String output = httpReader.post(url, JsonCodec.INSTANCE.toJson(request));
			OrderDTO order = JsonCodec.INSTANCE.parseJson(output, OrderDTO.class);
			return order;
		} catch (IOException | JsonParsingException e) {
			throw new StockServiceException(e);
		}		
	}

}
