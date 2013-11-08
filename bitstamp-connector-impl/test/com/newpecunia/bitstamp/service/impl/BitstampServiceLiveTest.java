package com.newpecunia.bitstamp.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.newpecunia.bitstamp.BitstampConnectorModule;
import com.newpecunia.bitstamp.service.AccountBalance;
import com.newpecunia.bitstamp.service.BitstampService;
import com.newpecunia.bitstamp.service.BitstampServiceException;
import com.newpecunia.bitstamp.service.EurUsdRate;
import com.newpecunia.bitstamp.service.Order;
import com.newpecunia.bitstamp.service.OrderBook;
import com.newpecunia.bitstamp.service.PriceAndAmount;
import com.newpecunia.bitstamp.service.Ticker;
import com.newpecunia.bitstamp.service.Transaction;
import com.newpecunia.bitstamp.service.UnconfirmedBitcoinDeposit;
import com.newpecunia.bitstamp.service.UserTransaction;
import com.newpecunia.common.CommonModule;

public class BitstampServiceLiveTest {

	private static final int WAIT_FOR_ACTUAL_OVERVIEW_MS = 10000;
	private BitstampService bitstampService = null;

	@Before
	public void setup() {
		Injector injector = Guice.createInjector(new BitstampConnectorModule(), new CommonModule());
		bitstampService = injector.getInstance(BitstampService.class);
	}

	@Test
	public void testLiveTicker() throws Exception {
		Ticker ticker = bitstampService.getTicker();
		assertNotNull(ticker.getHigh());
		assertNotNull(ticker.getLast());
		assertNotNull(ticker.getTimestamp());
		assertNotNull(ticker.getBid());
		assertNotNull(ticker.getVolume());
		assertNotNull(ticker.getLow());
		assertNotNull(ticker.getAsk());
	}
	
	@Test
	@Ignore
	public void testLiveBuyLimitOrder() throws Exception {
		//CAUTION: the following live test will perform REAL transaction with REAL money!
		bitstampService.buyLimitOrder(new BigDecimal("0.1"), new BigDecimal("10"));
	}
	
	@Test
	@Ignore
	public void testLiveSellLimitOrder() throws Exception {
		//CAUTION: the following live test will perform REAL transaction with REAL money!
		bitstampService.sellLimitOrder(new BigDecimal("10000"), new BigDecimal("1"));
	}	
	
	@Test
	@Ignore
	public void testLiveCancelOrder() throws Exception {
		//CAUTION: the following live test will perform REAL transaction with REAL money!
		bitstampService.cancelOrder("123"); //TODO set here your order ID
	}

	@Test
	public void testLiveGetOpenOrders() throws Exception {
		List<Order> orders = bitstampService.getOpenOrders();		
		assertNotNull(orders);	
		for (Order order : orders) {
			assertNotNull(order.getAmount());
			assertNotNull(order.getDatetime());
			assertNotNull(order.getId());
			assertNotNull(order.getOrderType());
			assertNotNull(order.getPrice());
		}
	}
	
	@Test
	public void testLiveGetEurUsdConversionRate() throws Exception {
		EurUsdRate rate = bitstampService.getEurUsdConversionRate();
		assertTrue(rate.getSellEurRate().compareTo(rate.getBuyEurRate()) < 0);
	}
	
	@Test
	public void testLiveGetAccountBalance() throws Exception {
		AccountBalance balance = bitstampService.getAccountBalance();
		assertNotNull(balance);
		assertNotNull(balance.getBtcAvailable());
		assertNotNull(balance.getBtcBalance());
		assertNotNull(balance.getBtcReserved());
		assertNotNull(balance.getCustomerTradingPercentFee());
		assertNotNull(balance.getUsdAvailable());
		assertNotNull(balance.getUsdBalance());
		assertNotNull(balance.getUsdReserved());
	}
	
	@Test
	public void testLiveGetBitcoinDepositAddress() throws Exception {
		String depositAddress = bitstampService.getBitcoinDepositAddress();
		assertNotNull(depositAddress);
		assertTrue(depositAddress.length() > 10);
	}
	
	@Test
	public void testLiveGetUserTransactions() throws Exception {
		List<UserTransaction> userTransactions = bitstampService.getUserTransactions(0, 10);
		assertNotNull(userTransactions);
		UserTransaction lastUserTransaction = null;
		for (UserTransaction userTransaction : userTransactions) {
			assertNotNull(userTransaction.getAmountBTC());
			assertNotNull(userTransaction.getAmountUSD());
			assertNotNull(userTransaction.getBtcPrice());
			assertNotNull(userTransaction.getDatetime());
//			assertNotNull(userTransaction.getOrderId()); - for some reason the Bitstamp service returns null as order ID.
			assertNotNull(userTransaction.getTransactionFee());
			assertNotNull(userTransaction.getTransactionId());
			assertNotNull(userTransaction.getType());
			if (lastUserTransaction != null) {
				assertTrue(lastUserTransaction.getDatetime().getMillis() >= userTransaction.getDatetime().getMillis());
				assertTrue(Long.parseLong(lastUserTransaction.getTransactionId()) > Long.parseLong(userTransaction.getTransactionId()));
			}
			
			lastUserTransaction = userTransaction;
		}		
	}
	
	@Test
	public void testLiveGetTransactions() throws Exception {
		List<Transaction> transactions = bitstampService.getTransactions(0, 100);
		assertNotNull(transactions);
		Transaction lastTransaction = null;
		for (Transaction transaction : transactions) {
			assertNotNull(transaction.getBtcAmount());
			assertNotNull(transaction.getBtcPrice());
			assertNotNull(transaction.getTimestamp());
			assertNotNull(transaction.getTransactionId());
			if (lastTransaction != null) {
				assertTrue(lastTransaction.getTimestamp().getMillis() >= transaction.getTimestamp().getMillis());
				assertTrue(Long.parseLong(lastTransaction.getTransactionId()) > Long.parseLong(transaction.getTransactionId()));
			}
			lastTransaction = transaction;
		}
	}

	@Test
	public void testLiveGetUnconfirmedBitcoinDeposits() throws Exception {
		List<UnconfirmedBitcoinDeposit> unconfirmedDeposits = bitstampService.getUnconfirmedBitcoinDeposits();
		assertNotNull(unconfirmedDeposits);
	}
	
	
	@Test
	public void testSellGetCancel() throws Exception {
		//CAUTION: the following live test will perform REAL transactions. However without loosing money if it does not fail.
		List<Order> ordersBeforeBuy = waitAndGetOpenOrders();
		assertEquals("The test cannot start because some orders are already present.", 0, ordersBeforeBuy.size()); //check before the test starts

		Order order = bitstampService.sellLimitOrder(new BigDecimal("10000"), new BigDecimal("0.01"));
		List<Order> orders = waitAndGetOpenOrders();
		assertEquals(1, orders.size());
		assertEquals(order.getId(), orders.get(0).getId());
		boolean result = bitstampService.cancelOrder(order.getId());
		assertTrue(result);
		orders = waitAndGetOpenOrders();
		assertEquals(0, orders.size());
	}
	
	@Test
	public void testBuyGetCancel() throws Exception {
		//CAUTION: the following live test will perform REAL transactions. However without loosing money if it does not fail.
		List<Order> ordersBeforeBuy = waitAndGetOpenOrders();
		assertEquals("The test cannot start because some orders are already present.", 0, ordersBeforeBuy.size()); //check before the test starts
		
		Order order = bitstampService.buyLimitOrder(new BigDecimal("10"), new BigDecimal("10"));
		List<Order> ordersAfterBuy = waitAndGetOpenOrders();
		assertEquals(1, ordersAfterBuy.size());
		assertEquals(order.getId(), ordersAfterBuy.get(0).getId());
		boolean result = bitstampService.cancelOrder(order.getId());
		assertTrue(result);
		List<Order> ordersAfterCancel = waitAndGetOpenOrders();
		assertEquals(0, ordersAfterCancel.size());
	}
	
	@Test
	@Ignore
	public void testBuyForRealPrice() throws Exception {
		//CAUTION: the following live test will perform REAL transactions - it will buy BTC for real price		
		BigDecimal wantToBuy = new BigDecimal("0.1");
		
		BigDecimal btcBalanceBefore = bitstampService.getAccountBalance().getBtcBalance();
		
		OrderBook orderBook = bitstampService.getOrderBook();
		List<PriceAndAmount> asks = orderBook.getAsks();
		BigDecimal amountRest = wantToBuy.multiply(BigDecimal.ONE); //clone
		for (PriceAndAmount ask : asks) {
			BigDecimal askPrice = ask.getPrice();
			BigDecimal askAmount = ask.getAmount();
			BigDecimal amountToBuy; 
			if (amountRest.compareTo(askAmount) < 0) {
				amountToBuy = amountRest;
			} else {
				amountToBuy = askAmount;
			}			
			if (amountToBuy.compareTo(BigDecimal.ZERO) != 0) {
				System.out.println("Buying "+amountToBuy.toPlainString()+" BTC for $"+askPrice.toPlainString());
				bitstampService.buyLimitOrder(askPrice, amountToBuy);
				amountRest = amountRest.subtract(amountToBuy);
			} else {
				break;
			}
		}
		
		List<Order> orders = waitAndGetOpenOrders();
		assertEquals("Orders should be performed", 0, orders.size());
		BigDecimal btcBalanceAfter = bitstampService.getAccountBalance().getBtcBalance();
		assertTrue("BTC were bought", btcBalanceAfter.compareTo(btcBalanceBefore) > 0);
		assertTrue("BTC must be bought exactly", btcBalanceAfter.subtract(btcBalanceBefore).compareTo(wantToBuy) == 0);
	}
	
	/**
	 * This test sends money to my Bitcoin address in Electum wallet
	 * @throws Exception
	 */
	@Test
	@Ignore
	public void testBitcoinWithdrawal() throws Exception {
		//CAUTION: the following live test will perform REAL operation with REAL money (BTC).
		Boolean status = bitstampService.bitcoinWithdrawal(new BigDecimal("0.00006"), "1wGjmUwFLqfL2C15BVUACCDosVRFNbfcR"); //in my Electrum wallet
		assertTrue(status);
	}
	
	private List<Order> waitAndGetOpenOrders() throws BitstampServiceException, InterruptedException {
		Thread.sleep(WAIT_FOR_ACTUAL_OVERVIEW_MS);
		return bitstampService.getOpenOrders();		
	}
	
	@Test
	@Ignore
	public void cancelAllOpenedOrders() throws Exception {
		//CAUTION: NEVER run this test with PRODUCTION account! You would DELETE ALL opened transactions! 
		//  run it ONLY if you want to clean up all orders of your TEST account
		List<Order> orders = waitAndGetOpenOrders();
		for (Order order : orders) {
			System.out.println("Canceling order "+order.getId());
			boolean cancelResult = bitstampService.cancelOrder(order.getId());
			assertTrue(cancelResult);
		}
		orders = waitAndGetOpenOrders();
		assertEquals(0, orders.size());
	}
	
	
}
