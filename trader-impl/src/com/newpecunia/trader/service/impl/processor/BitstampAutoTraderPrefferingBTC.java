package com.newpecunia.trader.service.impl.processor;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.newpecunia.NPException;
import com.newpecunia.bitcoind.service.BitcoindService;
import com.newpecunia.bitstamp.service.BitstampService;
import com.newpecunia.bitstamp.service.BitstampServiceException;
import com.newpecunia.bitstamp.webdriver.BitstampWebdriver;
import com.newpecunia.bitstamp.webdriver.BitstampWebdriverException;
import com.newpecunia.bitstamp.webdriver.InternationalWithdrawRequest;
import com.newpecunia.configuration.NPConfiguration;
import com.newpecunia.trader.NotEnoughBtcException;
import com.newpecunia.trader.service.impl.AvgPriceCalculator;
import com.newpecunia.trader.service.impl.CachedBuySellPriceCalculator;

//TODO reimplement me

@Singleton
public class BitstampAutoTraderPrefferingBTC extends AbstractBitstampAutoTrader {

	private NPConfiguration configuration;
	private BitstampWebdriver bitstampWebdriver;
	private BitcoindService bitcoindService;

	@Inject
	BitstampAutoTraderPrefferingBTC(BitstampService bitstampService,
			BitstampWebdriver bitstampWebdriver,
			BitcoindService bitcoindService,
			CachedBuySellPriceCalculator cachedBuySellPriceCalculator,
			NPConfiguration configuration) {
		
		super(bitstampService, cachedBuySellPriceCalculator);
		this.bitstampWebdriver = bitstampWebdriver;
		this.bitcoindService = bitcoindService;
		this.configuration = configuration;
	}

	@Override
	public void sendUsdToUnicredit(BigDecimal amountUSD) {
		try {
			BigDecimal sellPrice = AvgPriceCalculator.calculateAvgBtcPriceForUSDs(bitstampService.getOrderBook().getBids(),
					amountUSD.multiply(new BigDecimal("1.3"))); //multiply by 1.3 - be on the safe side to be sure that the sell request will be processed immediately
			
			BigDecimal amountBTC = amountUSD.divide(sellPrice, 8, RoundingMode.UP);
			
			bitstampService.sellLimitOrder(sellPrice, amountBTC);
			
			//wait 1 sec to process the order
			Thread.sleep(1000);
			
			//withdraw the money to Unicredit
			InternationalWithdrawRequest withdrawRequest = new InternationalWithdrawRequest();
			withdrawRequest.setAmount(amountUSD);
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
			
		} catch (BitstampServiceException | InterruptedException | IOException | BitstampWebdriverException e) {
			throw new NPException("Could not withdraw USD.",e );
		}
	}

	@Override
	public void sendBtcFromBitstampToWallet(BigDecimal amount) {
		if (getBalanceInBTC().compareTo(amount) < 0) {
			throw new NotEnoughBtcException();
		}
		
		try {
			bitstampService.bitcoinWithdrawal(amount, configuration.getBitcoindServerWalletAddress());
		} catch (BitstampServiceException e) {
			throw new NPException("Could not withdraw BTC to wallet.", e);
		}
	}

	@Override
	public void sendBtcFromWalletToBitstamp(BigDecimal amount) {
		String depositAddress = configuration.getBitstampBtcAddress();
		
//		try {
//			depositAddress = bitstampService.getBitcoinDepositAddress();
//		} catch (BitstampServiceException e) {
//			throw new NPException("Could not get Bitstamp deposit BTC address.", e);
//		}
		
		bitcoindService.sendMoney(depositAddress, amount, "To Bitstamp", "");
	}
	
}
