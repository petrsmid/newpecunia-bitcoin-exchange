package com.newpecunia.trader.service.impl.processor;

import java.math.BigDecimal;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.newpecunia.bitstamp.service.AccountBalance;
import com.newpecunia.bitstamp.service.BitstampService;
import com.newpecunia.bitstamp.webdriver.BitstampWebdriver;
import com.newpecunia.configuration.NPConfiguration;

@Singleton
public class BitstampAutoTraderPrefferingBTC extends AbstractBitstampAutoTrader implements BitstampAutoTrader {

	private BitstampWithdrawOrderManager bitstampWithdrawManager;
	private BitstampService bitstampService;
	private NPConfiguration configuration;

	@Inject
	BitstampAutoTraderPrefferingBTC(BitstampService bitstampService,
			BitstampWebdriver bitstampWebdriver,
			NPConfiguration configuration,
			BitstampBalance bitstampBalance,
			BitstampWithdrawOrderManager bitstampWithdrawManager) {
		
		super(bitstampService, bitstampWebdriver, configuration);
		
		this.bitstampWithdrawManager = bitstampWithdrawManager;
		this.bitstampService = bitstampService;
		this.configuration = configuration;
	}

	@Override
	public void trade() throws Exception {
		cancelPendingOrders();
		
		AccountBalance accountBalance = bitstampService.getAccountBalance();

		//withdraw BTCs
		BigDecimal btcToWithdraw = bitstampWithdrawManager.getBtcAmountToWithdraw();
		BigDecimal btcAvailable = accountBalance.getBtcAvailable().subtract(configuration.getBitcoindTransactionFee());

		BigDecimal btcToWithdrawNow = btcToWithdraw.min(btcAvailable);
		this.withdrawBTC(btcToWithdrawNow);
		
		//withdraw USD if possible at once and trade USD
		//TODO accumulate USD withdraw and send it once per day (start to collect 1 hour before daily withdrawal)
		BigDecimal usdAvailable = accountBalance.getUsdAvailable();
		BigDecimal usdToWithdraw = bitstampWithdrawManager.getUsdAmountToWithdraw();
		BigDecimal usdToWithdrawWithFee = usdToWithdraw.add(calculateWithdrawalUsdFee(usdToWithdraw));
		if (accountBalance.getUsdAvailable().compareTo(usdToWithdrawWithFee) >= 0) {
			this.withdrawUSD(usdToWithdraw);
			BigDecimal rest = usdAvailable.subtract(usdToWithdrawWithFee);
			buyBtcForUsd(rest);
		} else {
			BigDecimal usdToBuy = usdToWithdrawWithFee.subtract(usdAvailable);
			sellBtcToGetUsd(usdToBuy);
		}
		
	}
	
	private void cancelPendingOrders() {
		// TODO implement me
		
	}

	private void sellBtcToGetUsd(BigDecimal usdToGet) {
		// TODO implement me
		
	}

	private void buyBtcForUsd(BigDecimal rest) {
		// TODO implement me
		
	}

	private BigDecimal calculateWithdrawalUsdFee(BigDecimal amount) {
		BigDecimal percentFee = amount.multiply(configuration.getBitstampWithdrawUsdFeePercent().multiply(new BigDecimal("0.01")));
		return percentFee.max(configuration.getBitstampMinWithdrawUsdFee());
	}

}
