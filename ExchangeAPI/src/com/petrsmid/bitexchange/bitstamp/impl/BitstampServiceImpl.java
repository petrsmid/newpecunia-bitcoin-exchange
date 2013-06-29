package com.petrsmid.bitexchange.bitstamp.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.inject.Inject;
import com.petrsmid.bitexchange.bitstamp.BitstampService;
import com.petrsmid.bitexchange.bitstamp.BitstampServiceException;
import com.petrsmid.bitexchange.bitstamp.Order;
import com.petrsmid.bitexchange.bitstamp.OrderBook;
import com.petrsmid.bitexchange.bitstamp.Ticker;
import com.petrsmid.bitexchange.bitstamp.impl.dto.AccountBalanceDTO;
import com.petrsmid.bitexchange.bitstamp.impl.dto.ErrorDTO;
import com.petrsmid.bitexchange.bitstamp.impl.dto.EurUsdRateDTO;
import com.petrsmid.bitexchange.bitstamp.impl.dto.OrderBookDTO;
import com.petrsmid.bitexchange.bitstamp.impl.dto.OrderBookMapper;
import com.petrsmid.bitexchange.bitstamp.impl.dto.OrderDTO;
import com.petrsmid.bitexchange.bitstamp.impl.dto.OrderMapper;
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
	public Ticker getTicker() throws BitstampServiceException {
		String url = BitstampConstants.TICKER_URL;
		try {
			String output = httpReader.get(url);
			checkResponseForError(output);
			Ticker ticker = JsonCodec.INSTANCE.parseJson(output, Ticker.class);
			return ticker;
		} catch (IOException | JsonParsingException e) {
			throw new BitstampServiceException(e);
		}
	}

	@Override
	public OrderBook getOrderBook() throws BitstampServiceException {
		String url = BitstampConstants.ORDER_BOOK_URL;
		try {
			String output = httpReader.get(url);
			checkResponseForError(output);
			OrderBookDTO orderBookDTO = JsonCodec.INSTANCE.parseJson(output, OrderBookDTO.class);
			return OrderBookMapper.mapOrderBookDTO2OrderBook(orderBookDTO);
		} catch (IOException | JsonParsingException e) {
			throw new BitstampServiceException(e);
		}
	}

	@Override
	public Order buyLimitOrder(BigDecimal price, BigDecimal amount) throws BitstampServiceException {
		List<NameValuePair> requestParams = new ArrayList<>();
		requestParams.add(new BasicNameValuePair("user", credentials.getUsername()));		
		requestParams.add(new BasicNameValuePair("password", credentials.getPassword()));		
		requestParams.add(new BasicNameValuePair("amount", amount.toPlainString()));		
		requestParams.add(new BasicNameValuePair("price", price.toPlainString()));		
		
		String url = BitstampConstants.BUY_LIMIT_ORDER;
		try {
			String output = httpReader.post(url, requestParams);
			checkResponseForError(output);
			OrderDTO orderDTO = JsonCodec.INSTANCE.parseJson(output, OrderDTO.class);
			return OrderMapper.mapOrderDTO2Order(orderDTO);
		} catch (IOException | JsonParsingException e) {
			throw new BitstampServiceException(e);
		}		
	}

	
	@Override
	public Order sellLimitOrder(BigDecimal price, BigDecimal amount) throws BitstampServiceException {
		List<NameValuePair> requestParams = new ArrayList<>();
		requestParams.add(new BasicNameValuePair("user", credentials.getUsername()));		
		requestParams.add(new BasicNameValuePair("password", credentials.getPassword()));		
		requestParams.add(new BasicNameValuePair("amount", amount.toPlainString()));		
		requestParams.add(new BasicNameValuePair("price", price.toPlainString()));		

		String url = BitstampConstants.SELL_LIMIT_ORDER;
		try {
			String output = httpReader.post(url, requestParams);
			checkResponseForError(output);
			OrderDTO orderDTO = JsonCodec.INSTANCE.parseJson(output, OrderDTO.class);
			return OrderMapper.mapOrderDTO2Order(orderDTO);
		} catch (IOException | JsonParsingException e) {
			throw new BitstampServiceException(e);
		}		
	}
	
	@Override
	public Boolean cancelOrder(String orderId) throws BitstampServiceException {
		List<NameValuePair> requestParams = new ArrayList<>();
		requestParams.add(new BasicNameValuePair("user", credentials.getUsername()));		
		requestParams.add(new BasicNameValuePair("password", credentials.getPassword()));		
		requestParams.add(new BasicNameValuePair("id", orderId));
		
		String url = BitstampConstants.CANCEL_ORDER;
		try {
			String output = httpReader.post(url, requestParams);
			checkResponseForError(output);
			Boolean success = JsonCodec.INSTANCE.parseJson(output, Boolean.class);
			return success;
		} catch (IOException | JsonParsingException e) {
			throw new BitstampServiceException(e);
		}		
	}
	
	@Override
	public List<Order> getOpenOrders() throws BitstampServiceException {
		List<NameValuePair> requestParams = new ArrayList<>();
		requestParams.add(new BasicNameValuePair("user", credentials.getUsername()));		
		requestParams.add(new BasicNameValuePair("password", credentials.getPassword()));		
		
		String url = BitstampConstants.OPEN_ORDERS;
		try {
			String output = httpReader.post(url, requestParams);
			checkResponseForError(output);
			OrderDTO[] orderDTOs = JsonCodec.INSTANCE.parseJson(output, OrderDTO[].class);
			List<Order> orders = new ArrayList<>();
			for (OrderDTO orderDTO : orderDTOs) {
				orders.add(OrderMapper.mapOrderDTO2Order(orderDTO));
			}
			return orders;
		} catch (IOException | JsonParsingException e) {
			throw new BitstampServiceException(e);
		}
	}
	
	@Override
	public EurUsdRateDTO getEurUsdConversionRate() throws BitstampServiceException {
		String url = BitstampConstants.EUR_USD_RATE;
		try {
			String output = httpReader.get(url);
			checkResponseForError(output);
			EurUsdRateDTO eurUsdRate = JsonCodec.INSTANCE.parseJson(output, EurUsdRateDTO.class);
			return eurUsdRate;
		} catch (IOException | JsonParsingException e) {
			throw new BitstampServiceException(e);
		}			
	}
	
	@Override
	public AccountBalanceDTO getAccountBalance() throws BitstampServiceException {
		List<NameValuePair> requestParams = new ArrayList<>();
		requestParams.add(new BasicNameValuePair("user", credentials.getUsername()));		
		requestParams.add(new BasicNameValuePair("password", credentials.getPassword()));		
		
		String url = BitstampConstants.ACCOUNT_BALANCE;
		try {
			String output = httpReader.post(url, requestParams);
			checkResponseForError(output);
			AccountBalanceDTO accountBalance = JsonCodec.INSTANCE.parseJson(output, AccountBalanceDTO.class);
			return accountBalance;
		} catch (IOException | JsonParsingException e) {
			throw new BitstampServiceException(e);
		}		
	}
	
	
	@Override
	public Boolean bitcoinWithdrawal(BigDecimal amount, String address) throws BitstampServiceException {
		List<NameValuePair> requestParams = new ArrayList<>();
		requestParams.add(new BasicNameValuePair("user", credentials.getUsername()));		
		requestParams.add(new BasicNameValuePair("password", credentials.getPassword()));		
		requestParams.add(new BasicNameValuePair("amount", amount.toPlainString()));		
		requestParams.add(new BasicNameValuePair("address", address));		
		
		String url = BitstampConstants.BITCOIN_WITHDRAWAL;
		try {
			String output = httpReader.post(url, requestParams);
			checkResponseForError(output);
			Boolean success = JsonCodec.INSTANCE.parseJson(output, Boolean.class);
			return success;
		} catch (IOException | JsonParsingException e) {
			throw new BitstampServiceException(e);
		}		
	}
	
	@Override
	public String getBitcoinDepositAddress() throws BitstampServiceException {
		List<NameValuePair> requestParams = new ArrayList<>();
		requestParams.add(new BasicNameValuePair("user", credentials.getUsername()));		
		requestParams.add(new BasicNameValuePair("password", credentials.getPassword()));		
		
		String url = BitstampConstants.BITCOIN_DEPOSIT_ADDRESS;
		try {
			String output = httpReader.post(url, requestParams);
			checkResponseForError(output);
			String address = JsonCodec.INSTANCE.parseJson(output, String.class);
			return address;
		} catch (IOException | JsonParsingException e) {
			throw new BitstampServiceException(e);
		}		
	}
	
	private void checkResponseForError(String output) throws BitstampServiceException {
		if (output.contains("error")) {
			try { //try to parse the json error object and report the reason
				ErrorDTO error = JsonCodec.INSTANCE.parseJson(output, ErrorDTO.class);
				throw new BitstampServiceException("Error returned from the Bitstamp service. Reason: " + error.getError());
			} catch (JsonParsingException e) { //parsing unsuccessful -> report the whole response
				throw new BitstampServiceException("Error returned from the Bitstamp service: " + output);
			}			
		}
	}

}
