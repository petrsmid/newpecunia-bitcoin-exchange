package com.petrsmid.bitexchange.bitstamp;

import java.math.BigDecimal;

import com.google.inject.Inject;
import com.petrsmid.bitexchange.StockExchange;
import com.petrsmid.bitexchange.StockServiceException;

public class Bitstamp implements StockExchange {

	private TickerService tickerService;

	@Inject
	public Bitstamp(TickerService tickerService) {
		this.tickerService = tickerService;
	}
	
	@Override
	public BigDecimal getBuyBtcPrice_USD(BigDecimal amountOfBtc) throws StockServiceException {
		return tickerService.getTicker().getAsk();
	}

	@Override
	public BigDecimal getSellBtcPrice_USD(BigDecimal amountOfBtc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getStockReserve_USD() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getBtcAvailabeForTrading() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getMoneyAvailableForTrading_USD() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean withdrawBTC(BigDecimal amount, String address) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean withdrawUSD(BigDecimal amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
