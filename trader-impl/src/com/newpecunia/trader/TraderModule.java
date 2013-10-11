package com.newpecunia.trader;

import com.google.inject.AbstractModule;
import com.newpecunia.trader.service.TraderService;
import com.newpecunia.trader.service.impl.TraderServiceImpl;

public class TraderModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(TraderService.class).to(TraderServiceImpl.class);
	}

}
