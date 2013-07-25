package com.newpecunia.bitstamp.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.inject.Inject;
import com.newpecunia.bitstamp.service.AccountBalance;
import com.newpecunia.bitstamp.service.BitstampService;
import com.newpecunia.bitstamp.service.BitstampServiceException;
import com.newpecunia.bitstamp.service.EurUsdRate;
import com.newpecunia.bitstamp.service.Order;
import com.newpecunia.bitstamp.service.OrderBook;
import com.newpecunia.bitstamp.service.Ticker;
import com.newpecunia.bitstamp.service.Transaction;
import com.newpecunia.bitstamp.service.UnconfirmedBitcoinDeposit;
import com.newpecunia.bitstamp.service.UserTransaction;
import com.newpecunia.bitstamp.service.impl.dto.AccountBalanceDTO;
import com.newpecunia.bitstamp.service.impl.dto.AccountBalanceMapper;
import com.newpecunia.bitstamp.service.impl.dto.ErrorDTO;
import com.newpecunia.bitstamp.service.impl.dto.EurUsdRateDTO;
import com.newpecunia.bitstamp.service.impl.dto.OrderBookDTO;
import com.newpecunia.bitstamp.service.impl.dto.OrderBookMapper;
import com.newpecunia.bitstamp.service.impl.dto.OrderDTO;
import com.newpecunia.bitstamp.service.impl.dto.OrderMapper;
import com.newpecunia.bitstamp.service.impl.dto.TransactionDTO;
import com.newpecunia.bitstamp.service.impl.dto.TransactionMapper;
import com.newpecunia.bitstamp.service.impl.dto.UserTransactionDTO;
import com.newpecunia.bitstamp.service.impl.dto.UserTransactionMapper;
import com.newpecunia.net.HttpReader;
import com.newpecunia.net.HttpReaderFactory;
import com.newpecunia.net.JsonCodec;
import com.newpecunia.net.JsonParsingException;

public class BitstampServiceImpl implements BitstampService {

	private BitstampCredentials credentials;
	private HttpReader httpReader;
	
	@Inject
	public BitstampServiceImpl(HttpReaderFactory httpReaderFactory, BitstampCredentials credentials) {
		this.httpReader = httpReaderFactory.createNewHttpSimpleReader();
		this.credentials = credentials;
	}
	
	@Override
	public Ticker getTicker() throws BitstampServiceException {
		String url = BitstampServiceConstants.TICKER_URL;
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
		String url = BitstampServiceConstants.ORDER_BOOK_URL;
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
		
		String url = BitstampServiceConstants.BUY_LIMIT_ORDER;
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

		String url = BitstampServiceConstants.SELL_LIMIT_ORDER;
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
		
		String url = BitstampServiceConstants.CANCEL_ORDER;
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
		
		String url = BitstampServiceConstants.OPEN_ORDERS;
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
	public EurUsdRate getEurUsdConversionRate() throws BitstampServiceException {
		String url = BitstampServiceConstants.EUR_USD_RATE;
		try {
			String output = httpReader.get(url);
			checkResponseForError(output);
			EurUsdRateDTO eurUsdRateDTO = JsonCodec.INSTANCE.parseJson(output, EurUsdRateDTO.class);
			return new EurUsdRate(eurUsdRateDTO.getBuy(), eurUsdRateDTO.getSell());
		} catch (IOException | JsonParsingException e) {
			throw new BitstampServiceException(e);
		}			
	}
	
	@Override
	public AccountBalance getAccountBalance() throws BitstampServiceException {
		List<NameValuePair> requestParams = new ArrayList<>();
		requestParams.add(new BasicNameValuePair("user", credentials.getUsername()));		
		requestParams.add(new BasicNameValuePair("password", credentials.getPassword()));		
		
		String url = BitstampServiceConstants.ACCOUNT_BALANCE;
		try {
			String output = httpReader.post(url, requestParams);
			checkResponseForError(output);
			AccountBalanceDTO accountBalanceDTO = JsonCodec.INSTANCE.parseJson(output, AccountBalanceDTO.class);
			return AccountBalanceMapper.mapAccountBalanceDTO2AccountBalance(accountBalanceDTO);
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
		
		String url = BitstampServiceConstants.BITCOIN_WITHDRAWAL;
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
		
		String url = BitstampServiceConstants.BITCOIN_DEPOSIT_ADDRESS;
		try {
			String output = httpReader.post(url, requestParams);
			checkResponseForError(output);
			String address = JsonCodec.INSTANCE.parseJson(output, String.class);
			return address;
		} catch (IOException | JsonParsingException e) {
			throw new BitstampServiceException(e);
		}		
	}
	
	@Override
	public List<UserTransaction> getUserTransactions(long limit) throws BitstampServiceException {
		return getUserTransactions(0, limit);
	}

	@Override
	public List<UserTransaction> getUserTransactions(long offset, long limit) throws BitstampServiceException {
		List<NameValuePair> requestParams = new ArrayList<>();
		requestParams.add(new BasicNameValuePair("user", credentials.getUsername()));		
		requestParams.add(new BasicNameValuePair("password", credentials.getPassword()));		
		requestParams.add(new BasicNameValuePair("offset", Long.toString(offset)));
		requestParams.add(new BasicNameValuePair("limit", Long.toString(limit)));
		
		String url = BitstampServiceConstants.USER_TRANSACTIONS;
		try {
			String output = httpReader.post(url, requestParams);
			checkResponseForError(output);
			UserTransactionDTO[] transactionDTOs = JsonCodec.INSTANCE.parseJson(output, UserTransactionDTO[].class);
			List<UserTransaction> userTransactions = new ArrayList<>();
			for (UserTransactionDTO dto : transactionDTOs) {
				userTransactions.add(UserTransactionMapper.mapUserTransactionDTO2UserTransaction(dto));
			}
			return userTransactions;
		} catch (IOException | JsonParsingException e) {
			throw new BitstampServiceException(e);
		}		
	}
	
	@Override
	public List<Transaction> getTransactions(long limit) throws BitstampServiceException {
		return getTransactions(0, limit);
	}
	
	@Override
	public List<Transaction> getTransactions(long offset, long limit) throws BitstampServiceException {
		String url = BitstampServiceConstants.TRANSACTIONS;
		try {
			String output = httpReader.get(url+"?offset="+offset+"&limit="+limit);
			checkResponseForError(output);
			TransactionDTO[] transactionDTOs = JsonCodec.INSTANCE.parseJson(output, TransactionDTO[].class);
			List<Transaction> transactions = new ArrayList<>();
			for (TransactionDTO transactionDTO : transactionDTOs) {
				transactions.add(TransactionMapper.mapTransactionDTO2Transaction(transactionDTO));
			}
			return transactions;
		} catch (IOException | JsonParsingException e) {
			throw new BitstampServiceException(e);
		}		
	}	
	
	@Override
	public List<UnconfirmedBitcoinDeposit> getUnconfirmedBitcoinDeposits() throws BitstampServiceException {
		List<NameValuePair> requestParams = new ArrayList<>();
		requestParams.add(new BasicNameValuePair("user", credentials.getUsername()));		
		requestParams.add(new BasicNameValuePair("password", credentials.getPassword()));		
		
		String url = BitstampServiceConstants.UNCONFIRMED_BTC;
		try {
			String output = httpReader.post(url, requestParams);
			checkResponseForError(output);
			UnconfirmedBitcoinDeposit[] unconfirmedBtcDeposits = JsonCodec.INSTANCE.parseJson(output, UnconfirmedBitcoinDeposit[].class);
			return Arrays.asList(unconfirmedBtcDeposits);
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
