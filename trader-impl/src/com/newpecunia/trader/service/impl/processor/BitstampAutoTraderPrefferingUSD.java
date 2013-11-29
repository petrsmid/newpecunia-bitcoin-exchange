package com.newpecunia.trader.service.impl.processor;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.newpecunia.bitstamp.service.BitstampService;
import com.newpecunia.bitstamp.webdriver.BitstampWebdriver;
import com.newpecunia.configuration.NPConfiguration;

//TODO implement me


@Singleton
public class BitstampAutoTraderPrefferingUSD extends AbstractBitstampAutoTrader implements BitstampAutoTrader {

	private BitstampWithdrawOrderManager bitstampWithdrawRequestor;

	@Inject
	BitstampAutoTraderPrefferingUSD(BitstampService bitstampService,
			BitstampWebdriver bitstampWebdriver,
			NPConfiguration configuration,
			BitstampWithdrawOrderManager bitstampWithdrawRequestor) {
		super(bitstampService, bitstampWebdriver, configuration	);
		
		this.bitstampWithdrawRequestor = bitstampWithdrawRequestor;
	}

	@Override
	public void trade() {
		// TODO Auto-generated method stub
		
	}


}
