package com.newpecunia.trader;

import com.google.inject.AbstractModule;
import com.newpecunia.trader.service.TraderService;
import com.newpecunia.trader.service.impl.TraderServiceImpl;
import com.newpecunia.trader.service.impl.processor.BitstampAutoTrader;
import com.newpecunia.trader.service.impl.processor.BitstampAutoTraderPrefferingBTC;
import com.newpecunia.trader.service.impl.processor.BitstampWithdrawOrderManager;
import com.newpecunia.trader.service.impl.processor.BitstampWithdrawOrderManagerImpl;

public class TraderModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(TraderService.class).to(TraderServiceImpl.class);
		bind(BitstampAutoTrader.class).to(BitstampAutoTraderPrefferingBTC.class); //If you prefer holding USDs change to BitstampAutoTraderPrefferingUSD.class
		bind(BitstampWithdrawOrderManager.class).to(BitstampWithdrawOrderManagerImpl.class);
	}

}
