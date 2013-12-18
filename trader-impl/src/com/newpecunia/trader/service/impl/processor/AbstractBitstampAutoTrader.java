package com.newpecunia.trader.service.impl.processor;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.newpecunia.bitstamp.service.BitstampService;
import com.newpecunia.bitstamp.service.BitstampServiceException;
import com.newpecunia.bitstamp.service.Order;
import com.newpecunia.bitstamp.service.OrderBook;
import com.newpecunia.bitstamp.service.PriceAndAmount;
import com.newpecunia.bitstamp.webdriver.BitstampWebdriver;
import com.newpecunia.bitstamp.webdriver.BitstampWebdriverException;
import com.newpecunia.bitstamp.webdriver.InternationalWithdrawRequest;
import com.newpecunia.configuration.NPConfiguration;

public abstract class AbstractBitstampAutoTrader implements BitstampAutoTrader {

	private static final Logger logger = LogManager.getLogger(AbstractBitstampAutoTrader.class);	
	
	private BitstampWebdriver bitstampWebdriver;
	private NPConfiguration configuration;
	private BitstampService bitstampService;


	AbstractBitstampAutoTrader(BitstampService bitstampService, BitstampWebdriver bitstampWebdriver, NPConfiguration configuration) {
		this.bitstampService = bitstampService;
		this.bitstampWebdriver = bitstampWebdriver;
		this.configuration = configuration;
	}
	
	
	protected void withdrawUSD(BigDecimal amount) throws IOException, BitstampWebdriverException {
		logger.info("Withdrawing "+amount.toPlainString()+" USD to Unicredit account.");
		
		InternationalWithdrawRequest withdrawRequest = new InternationalWithdrawRequest();
		withdrawRequest.setAmount(amount);
		withdrawRequest.setCurrency(configuration.getBitstampWithdrawAccountCurrency());
		withdrawRequest.setName(configuration.getBitstampWithdrawAccountName());
		withdrawRequest.setAddress(configuration.getBitstampWithdrawAccountAddress());
		withdrawRequest.setCity(configuration.getBitstampWithdrawAccountCity());
		withdrawRequest.setPostalCode(configuration.getBitstampWithdrawAccountPostalCode());
		withdrawRequest.setCountry(configuration.getBitstampWithdrawAccountCountry());
		withdrawRequest.setBankName(configuration.getBitstampWithdrawAccountBankName());
		withdrawRequest.setBankAddress(configuration.getBitstampWithdrawAccountBankAddress());
		withdrawRequest.setBankCity(configuration.getBitstampWithdrawAccountBankCity());
		withdrawRequest.setBankPostalCode(configuration.getBitstampWithdrawAccountBankPostalCode());
		withdrawRequest.setBankCountry(configuration.getBitstampWithdrawAccountBankCountry());
		withdrawRequest.setIban(configuration.getBitstampWithdrawAccountIban());
		withdrawRequest.setBic(configuration.getBitstampWithdrawAccountBic());
		withdrawRequest.setComment(configuration.getBitstampWithdrawAccountComment());
		
		bitstampWebdriver.createInternationalWithdraw(withdrawRequest);
	}
	
	protected void withdrawBTC(BigDecimal amount) throws BitstampServiceException {
		logger.info("Withdrawing "+amount.toPlainString()+" BTC to server wallet.");
		bitstampService.bitcoinWithdrawal(amount, configuration.getBitcoindServerWalletAddress());
	}
	
	protected BigDecimal getWorstSellPrice(BigDecimal amount, OrderBook orderBook) {
		logger.trace("Getting worst sell price for amount "+amount.toPlainString());
		return getWorstPriceForAmount(amount, orderBook.getBids());
	}
	
	protected BigDecimal getWorstBuyPrice(BigDecimal amount, OrderBook orderBook) {
		logger.trace("Getting worst buy price for amount "+amount.toPlainString());
		return getWorstPriceForAmount(amount, orderBook.getAsks());
	}
	
	private BigDecimal getWorstPriceForAmount(BigDecimal amount, List<PriceAndAmount> orders) {
		BigDecimal sum = BigDecimal.ZERO;
		BigDecimal worstPrice = null;
		for (PriceAndAmount order : orders) {
			sum.add(order.getAmount());
			if (sum.compareTo(amount) >= 0) {
				worstPrice = order.getPrice();
				break;
			}
		}		
		return worstPrice;		
	}

	protected BigDecimal calculateWithdrawalUsdFee(BigDecimal amount) {
		BigDecimal percentFee = amount.multiply(configuration.getBitstampWithdrawUsdFeePercent().multiply(new BigDecimal("0.01")));
		return percentFee.max(configuration.getBitstampMinWithdrawUsdFee());
	}
	
	protected void cancelPendingOrders() throws BitstampServiceException {
		logger.trace("Checking unprocessed orders to cancel.");
		List<Order> openedOrders = bitstampService.getOpenOrders();
		if (!openedOrders.isEmpty()) {
			logger.error("Some orders were not fullfilled by the Bitstamp. Canceling the orders. Check it - maybe the Bitstamp uses longer caching. Adapt this job.");
			for (Order order : openedOrders) {
				logger.warn("Canceling order "+order.getId());
				bitstampService.cancelOrder(order.getId());
			}
		}
	}

	
}
