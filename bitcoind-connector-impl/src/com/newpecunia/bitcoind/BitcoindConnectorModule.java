package com.newpecunia.bitcoind;

import com.google.inject.AbstractModule;
import com.newpecunia.bitcoind.service.BitcoindService;
import com.newpecunia.bitcoind.service.impl.BitcoindServiceImpl;

public class BitcoindConnectorModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(BitcoindService.class).to(BitcoindServiceImpl.class);
	}

}
