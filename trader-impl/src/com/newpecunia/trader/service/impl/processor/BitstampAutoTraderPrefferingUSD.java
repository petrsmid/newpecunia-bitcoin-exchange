package com.newpecunia.trader.service.impl.processor;

import java.io.IOException;
import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.newpecunia.bitstamp.service.AccountBalance;
import com.newpecunia.bitstamp.service.BitstampService;
import com.newpecunia.bitstamp.service.BitstampServiceException;
import com.newpecunia.bitstamp.service.OrderBook;
import com.newpecunia.bitstamp.webdriver.BitstampWebdriver;
import com.newpecunia.bitstamp.webdriver.BitstampWebdriverException;
import com.newpecunia.configuration.NPConfiguration;


@Singleton
public class BitstampAutoTraderPrefferingUSD extends AbstractBitstampAutoTrader implements BitstampAutoTrader {

	private static final Logger logger = LogManager.getLogger(BitstampAutoTraderPrefferingUSD.class);	

	private static final BigDecimal PERCENT_107 = new BigDecimal("1.07"); 
	private static final BigDecimal PERCENT_93 = new BigDecimal("0.93");	
	
	private BitstampWithdrawOrderManager bitstampWithdrawManager;
	private BitstampService bitstampService;
	private NPConfiguration configuration;


	@Inject
	BitstampAutoTraderPrefferingUSD(BitstampService bitstampService,
			BitstampWebdriver bitstampWebdriver,
			NPConfiguration configuration,
			BitstampWithdrawOrderManager bitstampWithdrawOrderManager) {
		super(bitstampService, bitstampWebdriver, configuration	);
		
		this.bitstampWithdrawManager = bitstampWithdrawOrderManager;
		this.bitstampService = bitstampService;
		this.configuration = configuration;
	}

	@Override
	public void withdrawAndTrade() throws Exception {
		cancelPendingOrders();

		AccountBalance bitstampBalance = bitstampService.getAccountBalance();
		BigDecimal btcBalance = bitstampBalance.getBtcAvailable();
		BigDecimal usdBalance = bitstampBalance.getUsdAvailable();

		BigDecimal btcToWithdraw = bitstampWithdrawManager.getBtcAmountToWithdraw();
		BigDecimal usdToWithdraw = bitstampWithdrawManager.getUsdAmountToWithdraw();
		
		BigDecimal btcBalanceAfterWithdrawing = btcBalance.subtract(btcToWithdraw);
		BigDecimal usdBalanceAfterWithdrawing = usdBalance.subtract(usdToWithdraw);

		//perform withdrawals of USDs and BTCs
		withdrawUSDs(usdBalance, usdToWithdraw); //TODO - do it only once per day
		withdrawBTCs(btcBalance, btcToWithdraw);
		
		trade(btcBalanceAfterWithdrawing);
		
	}

	private void trade(BigDecimal btcAvailableAfterWithdrawing) throws BitstampServiceException {
		OrderBook orderBook = bitstampService.getOrderBook();
		if (btcAvailableAfterWithdrawing.compareTo(BigDecimal.ZERO) < 0) {
			logger.info("Performing trading - buying BTCs");
			//buy BTCs
			BigDecimal amountToBuy = btcAvailableAfterWithdrawing.multiply(new BigDecimal(-1)).multiply(getFeeMultiplier());
			if (amountToBuy.compareTo(getMinimalOrderAmount()) < 0) {
				amountToBuy = getMinimalOrderAmount();
			}
			BigDecimal buyPrice = getWorstBuyPrice(amountToBuy, orderBook).multiply(PERCENT_107);
			
			bitstampService.buyLimitOrder(buyPrice, amountToBuy);
		} else if (btcAvailableAfterWithdrawing.compareTo(BigDecimal.ZERO) > 0) {
			logger.info("Performing trading - selling BTCs");
			//sell BTCs
			BigDecimal amountToSell = btcAvailableAfterWithdrawing;
			BigDecimal sellPrice = getWorstSellPrice(amountToSell, orderBook).multiply(PERCENT_93);
			if (amountToSell.compareTo(getMinimalOrderAmount()) >= 0) {
				bitstampService.sellLimitOrder(sellPrice, amountToSell);
			}
		}
	}

	private BigDecimal getFeeMultiplier() throws BitstampServiceException {		
		BigDecimal fee = bitstampService.getAccountBalance().getCustomerTradingPercentFee();
		return BigDecimal.ONE.add(new BigDecimal("0.01").multiply(fee));
	}
	
	private BigDecimal getMinimalOrderAmount() {
		return configuration.getBitstampMinimalBtcOrder();
	}

	private void withdrawBTCs(BigDecimal btcAvailable, BigDecimal btcToWithdraw) throws BitstampServiceException {
		BigDecimal btcToWithdrawImmediately = btcToWithdraw.min(btcAvailable);
		withdrawBTC(btcToWithdrawImmediately);
		bitstampWithdrawManager.withdrawOfBtcFullfilled(btcToWithdrawImmediately);
	}

	private void withdrawUSDs(BigDecimal usdAvailable, BigDecimal usdToWithdraw) throws IOException, BitstampWebdriverException {
		BigDecimal usdToWithdrawWithFee = usdToWithdraw.add(calculateWithdrawalUsdFee(usdToWithdraw));
		BigDecimal toWithdraw;
		if (usdAvailable.compareTo(usdToWithdrawWithFee) < 0) {
			logger.error(String.format("Unable to withdraw %s USD. Not enough USD on Bitstamp account! Withdrawing only %s USD.", usdToWithdrawWithFee.toPlainString(), usdAvailable.toPlainString()));
			toWithdraw = usdAvailable;
		} else {
			logger.info(String.format("Withdrawing %s USD from Bitstamp.", usdToWithdrawWithFee.toPlainString()));
			toWithdraw = usdToWithdrawWithFee;
		}
		withdrawUSD(toWithdraw);
		bitstampWithdrawManager.withdrawOfUsdFullfilled(toWithdraw);

	}



}
