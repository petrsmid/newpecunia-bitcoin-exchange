package com.newpecunia.trader.service.impl.processor;

import java.io.IOException;
import java.math.BigDecimal;

import com.newpecunia.bitstamp.service.BitstampService;
import com.newpecunia.bitstamp.service.BitstampServiceException;
import com.newpecunia.bitstamp.webdriver.BitstampWebdriver;
import com.newpecunia.bitstamp.webdriver.BitstampWebdriverException;
import com.newpecunia.bitstamp.webdriver.InternationalWithdrawRequest;
import com.newpecunia.configuration.NPConfiguration;

public abstract class AbstractBitstampAutoTrader implements BitstampAutoTrader {

	private BitstampWebdriver bitstampWebdriver;
	private NPConfiguration configuration;
	private BitstampService bitstampService;


	AbstractBitstampAutoTrader(BitstampService bitstampService, BitstampWebdriver bitstampWebdriver, NPConfiguration configuration) {
		this.bitstampService = bitstampService;
		this.bitstampWebdriver = bitstampWebdriver;
		this.configuration = configuration;
	}
	
	
	protected void withdrawUSD(BigDecimal amount) throws IOException, BitstampWebdriverException {
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
		bitstampService.bitcoinWithdrawal(amount, configuration.getBitcoindServerWalletAddress());
	}	
}
