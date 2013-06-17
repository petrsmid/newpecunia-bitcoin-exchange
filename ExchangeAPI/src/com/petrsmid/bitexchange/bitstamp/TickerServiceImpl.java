package com.petrsmid.bitexchange.bitstamp;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.petrsmid.bitexchange.StockServiceException;
import com.petrsmid.bitexchange.net.HttpReader;

public class TickerServiceImpl implements TickerService {

	private HttpReader httpReader;

	@Inject
	public TickerServiceImpl(HttpReader httpReader) {
		this.httpReader = httpReader;
	}
	
	@Override
	public Ticker getTicker() throws StockServiceException {
		String url = Constants.BITSTAMP_API_URL + "/ticker/";
		try {
			String output = httpReader.readUrl(url);
			Gson gson = new Gson();
			Ticker ticker = gson.fromJson(output, Ticker.class);
			
			return ticker;
		} catch (IOException e) {
			throw new StockServiceException(e);
		}
	}


}
