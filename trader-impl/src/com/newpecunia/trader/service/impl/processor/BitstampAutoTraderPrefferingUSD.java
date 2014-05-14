package com.newpecunia.trader.service.impl.processor;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.newpecunia.bitstamp.service.AccountBalance;
import com.newpecunia.bitstamp.service.BitstampService;
import com.newpecunia.bitstamp.service.BitstampServiceException;
import com.newpecunia.bitstamp.service.CachedOrderBookHolder;
import com.newpecunia.bitstamp.service.OrderBook;
import com.newpecunia.configuration.NPConfiguration;


@Singleton
public class BitstampAutoTraderPrefferingUSD extends AbstractBitstampAutoTrader implements BitstampAutoTrader {

	private static final Logger logger = LogManager.getLogger(BitstampAutoTraderPrefferingUSD.class);	

	private static final BigDecimal PERCENT_107 = new BigDecimal("1.07"); 
	
	private BitstampWithdrawOrderManager bitstampWithdrawManager;
	private BitstampService bitstampService;
	private NPConfiguration configuration;
	private CachedOrderBookHolder orderBookHolder;


	@Inject
	BitstampAutoTraderPrefferingUSD(BitstampService bitstampService,
			NPConfiguration configuration,
			BitstampWithdrawOrderManager bitstampWithdrawOrderManager,
			CachedOrderBookHolder orderBookHolder) {
		super(bitstampService, configuration);
		
		this.bitstampWithdrawManager = bitstampWithdrawOrderManager;
		this.bitstampService = bitstampService;
		this.configuration = configuration;
		this.orderBookHolder = orderBookHolder;
	}

	@Override
	public void withdrawAndTrade() throws Exception {
		cancelPendingOrders();

		AccountBalance bitstampBalance = bitstampService.getAccountBalance();
		BigDecimal btcBalance = bitstampBalance.getBtcAvailable();

		BigDecimal btcToWithdraw = bitstampWithdrawManager.getBtcAmountToWithdraw();
		
		BigDecimal btcBalanceAfterWithdrawing = btcBalance.subtract(btcToWithdraw);

		//perform withdrawals of BTCs
		withdrawBTCs(btcBalance, btcToWithdraw);
		
		trade(btcBalanceAfterWithdrawing);
		
	}

	private void withdrawBTCs(BigDecimal btcAvailable, BigDecimal btcToWithdraw) throws BitstampServiceException {
		BigDecimal btcToWithdrawImmediately = btcToWithdraw.min(btcAvailable);
		withdrawBTC(btcToWithdrawImmediately);
		bitstampWithdrawManager.withdrawOfBtcFullfilled(btcToWithdrawImmediately);
	}

	private void trade(BigDecimal btcAvailableAfterWithdrawing) throws BitstampServiceException {
		OrderBook orderBook = orderBookHolder.getOrderBook();
		if (orderBookHolder.isOrderBookActual() && btcAvailableAfterWithdrawing.compareTo(BigDecimal.ZERO) < 0) {
			logger.info("Performing trading - buying BTCs");
			//buy BTCs
			BigDecimal amountToBuy = btcAvailableAfterWithdrawing.multiply(new BigDecimal(-1)).multiply(getBitstampFeeMultiplier());
			if (amountToBuy.compareTo(getMinimalOrderAmount()) < 0) {
				amountToBuy = getMinimalOrderAmount();
			}
			BigDecimal buyPrice = getWorstBuyPrice(amountToBuy, orderBook).multiply(PERCENT_107);
			
			bitstampService.buyLimitOrder(buyPrice, amountToBuy);
		} 
	}

	private BigDecimal getBitstampFeeMultiplier() throws BitstampServiceException {		
		BigDecimal fee = bitstampService.getAccountBalance().getCustomerTradingPercentFee();
		return BigDecimal.ONE.add(new BigDecimal("0.01").multiply(fee));
	}
	
	private BigDecimal getMinimalOrderAmount() {
		return configuration.getBitstampMinimalBtcOrder();
	}


}
