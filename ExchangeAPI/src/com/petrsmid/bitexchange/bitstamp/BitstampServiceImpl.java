package com.petrsmid.bitexchange.bitstamp;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.inject.Inject;
import com.petrsmid.bitexchange.StockServiceException;
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
	public OrderBook getOrderBook() {
		return null;
	}

}
