package com.newpecunia.bitstamp;

import com.google.inject.AbstractModule;
import com.newpecunia.bitstamp.service.BitstampService;
import com.newpecunia.bitstamp.service.impl.BitstampServiceImpl;
import com.newpecunia.bitstamp.webdriver.BitstampWebdriver;
import com.newpecunia.bitstamp.webdriver.impl.BitstampWebdriverImpl;
import com.newpecunia.net.BitstampRequestCountLimitVerifier;
import com.newpecunia.net.HttpReaderFactory;
import com.newpecunia.net.HttpReaderFactoryImpl;
import com.newpecunia.net.RequestCountLimitVerifier;
import com.newpecunia.synchronization.ClusterLockProvider;
import com.newpecunia.synchronization.SingleNodeClusterLockProvider;

public class BitstampConnectorModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(HttpReaderFactory.class).to(HttpReaderFactoryImpl.class);
		bind(ClusterLockProvider.class).to(SingleNodeClusterLockProvider.class);
		
		bind(RequestCountLimitVerifier.class).to(BitstampRequestCountLimitVerifier.class);
		bind(BitstampService.class).to(BitstampServiceImpl.class);
		bind(BitstampWebdriver.class).to(BitstampWebdriverImpl.class);
		
	}

}
