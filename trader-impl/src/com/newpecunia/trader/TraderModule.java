package com.newpecunia.trader;

import com.google.inject.AbstractModule;
import com.newpecunia.trader.service.BuyService;
import com.newpecunia.trader.service.PriceService;
import com.newpecunia.trader.service.impl.BuyServiceImpl;
import com.newpecunia.trader.service.impl.PriceServiceImpl;
import com.newpecunia.trader.service.impl.processor.BitstampAutoTrader;
import com.newpecunia.trader.service.impl.processor.BitstampAutoTraderPrefferingUSD;
import com.newpecunia.trader.service.impl.processor.BitstampWithdrawOrderManager;
import com.newpecunia.trader.service.impl.processor.BitstampWithdrawOrderManagerImpl;

public class TraderModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(BuyService.class).to(BuyServiceImpl.class);
		bind(PriceService.class).to(PriceServiceImpl.class);
		bind(BitstampAutoTrader.class).to(BitstampAutoTraderPrefferingUSD.class);
		bind(BitstampWithdrawOrderManager.class).to(BitstampWithdrawOrderManagerImpl.class);
	}

}
