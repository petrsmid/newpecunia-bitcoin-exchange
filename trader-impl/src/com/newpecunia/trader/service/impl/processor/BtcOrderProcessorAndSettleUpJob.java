package com.newpecunia.trader.service.impl.processor;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.newpecunia.bitcoind.service.BitcoindService;
import com.newpecunia.bitstamp.service.BitstampService;
import com.newpecunia.bitstamp.service.BitstampServiceException;
import com.newpecunia.bitstamp.service.OrderBook;
import com.newpecunia.configuration.NPConfiguration;
import com.newpecunia.persistence.entities.BtcPaymentOrder;
import com.newpecunia.persistence.entities.BtcPaymentOrder.BtcOrderStatus;
import com.newpecunia.time.TimeProvider;
import com.newpecunia.trader.service.impl.AvgPriceCalculator;

@DisallowConcurrentExecution
public class BtcOrderProcessorAndSettleUpJob implements Job {
	
	private static final Logger logger = LogManager.getLogger(BtcOrderProcessorAndSettleUpJob.class);	
	
	private BitcoindService bitcoindService;
	private BitstampService bitstampService;
	private Provider<EntityManager> emProvider;
	private TimeProvider timeProvider;
	private NPConfiguration configuration;

	
	@Inject
	BtcOrderProcessorAndSettleUpJob(Provider<EntityManager> emProvider, 
			TimeProvider timeProvider, 
			BitcoindService bitcoindService,
			BitstampService bitstampService,
			NPConfiguration configuration) {
		this.emProvider = emProvider;
		this.timeProvider = timeProvider;
		this.bitcoindService = bitcoindService;
		this.bitstampService = bitstampService;
		this.configuration = configuration;
	}
	
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		BigDecimal neededAdditionalAmount = processPendingBtcOrders();
		
		settleUpTheBalances(neededAdditionalAmount);
	}


	private void settleUpTheBalances(BigDecimal neededAdditionalAmount) {
		try {
			BigDecimal actualBalance = bitcoindService.getBalance();
			OrderBook orderBook = bitstampService.getOrderBook();
			if (actualBalance.compareTo(configuration.getOptimalBtcWalletBalance()) < 0) {
				buyOnBitstampAndSendToWallet(orderBook, configuration.getOptimalBtcWalletBalance().add(neededAdditionalAmount).subtract(actualBalance));
			} else if (actualBalance.compareTo(configuration.getOptimalBtcWalletBalance()) > 0) {
				sellOnBitstamp(orderBook, actualBalance.subtract(configuration.getOptimalBtcWalletBalance()));
			}
		} catch (BitstampServiceException e) {
			logger.error("Unable to settle up the balances. Problems with Bitstamp.", e);
		}
	}


	private BigDecimal processPendingBtcOrders() {
		Session session = emProvider.get().unwrap(Session.class);
		session.clear();		
		@SuppressWarnings("unchecked")
		List<BtcPaymentOrder> unprocessedOrders = session.createCriteria(BtcPaymentOrder.class)
			.add(Restrictions.eq("status", BtcOrderStatus.UNPROCESSED))
			.addOrder(Order.asc("createTimestamp"))		
			.list();
		

		EntityTransaction tx = emProvider.get().getTransaction();
		tx.begin();
		
		BigDecimal stillUnprocessedAmount = BigDecimal.ZERO;
		try {
			for (BtcPaymentOrder order : unprocessedOrders) {
				BigDecimal balance = bitcoindService.getBalance();
				if (balance.compareTo(order.getAmount().add(configuration.getBitcoindTransactionFee())) >= 0) {
					try {
						logger.info("Sending BTC to customer: "+order.getAmount().toPlainString()+" BTC to address "+order.getAddress());
						bitcoindService.sendMoney(order.getAddress(), order.getAmount(), "To customer", "New Pecunia");
						order.setStatus(BtcOrderStatus.PROCESSED);
						order.setUpdateTimestamp(timeProvider.nowCalendar());
						session.update(order);
						session.flush();
					} catch (Exception e) {
						logger.error("Error ocurred while processing BTC order.", e);
						break;
					}
				} else {
					logger.info("Not enough BTC in wallet to send "+order.getAmount().toPlainString()
							+" BTC to address "+order.getAddress()+". Waiting until corresponding amount will be bought on Bitstamp.");
					stillUnprocessedAmount = stillUnprocessedAmount.add(order.getAmount());
				}
			}
		} finally {
			tx.commit();
		}
		session.clear();
		return stillUnprocessedAmount;
	}
	
	
	
	private void sellOnBitstamp(OrderBook orderBook, BigDecimal amountToSell) throws BitstampServiceException {
		if (amountToSell.compareTo(configuration.getBitstampMinimalOrder()) < 0) {
			return; //do nothing
		}

		logger.info("Selling "+amountToSell.toPlainString()+" BTC on Bitstamp.");
		
		String bitstampAddress = bitstampService.getBitcoinDepositAddress();
		bitcoindService.sendMoney(bitstampAddress, amountToSell, "to Bitstamp", "");
		
		BigDecimal sellPrice = AvgPriceCalculator.calculateAvgPrice(orderBook.getBids(), amountToSell.multiply(new BigDecimal("1.3"))); //1.3 - better be on the safe side so the order really immediately processes
		bitstampService.sellLimitOrder(sellPrice, amountToSell);
	}


	private void buyOnBitstampAndSendToWallet(OrderBook orderBook, BigDecimal howMuchToBuy) throws BitstampServiceException {
		if (howMuchToBuy.compareTo(configuration.getBitstampMinimalOrder()) < 0) {
			howMuchToBuy = configuration.getBitstampMinimalOrder();
		}

		logger.info("Buying "+howMuchToBuy.toPlainString()+" BTC on Bitstamp.");
		
		BigDecimal buyPrice = AvgPriceCalculator.calculateAvgPrice(orderBook.getAsks(), howMuchToBuy.multiply(new BigDecimal("1.3"))); //1.3 - better be on the safe side so the order really immediately processes
		bitstampService.buyLimitOrder(buyPrice, howMuchToBuy);
		try {
			Thread.sleep(1000); //wait to process the order
		} catch (InterruptedException e) {
			logger.error("Sleep interrupted.", e);
		}
		BigDecimal btcBalance = bitstampService.getAccountBalance().getBtcBalance();
		bitstampService.bitcoinWithdrawal(btcBalance.subtract(configuration.getBitstampMinBtcReserve()),
				configuration.getBitcoindServerWalletAddress());
	}



}
