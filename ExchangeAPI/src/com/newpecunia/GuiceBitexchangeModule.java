package com.newpecunia;

import com.google.inject.AbstractModule;
import com.newpecunia.bitstamp.service.BitstampService;
import com.newpecunia.bitstamp.service.impl.BitstampCredentials;
import com.newpecunia.bitstamp.service.impl.BitstampServiceImpl;
import com.newpecunia.bitstamp.service.impl.SecureBitstampCredentialsImpl;
import com.newpecunia.bitstamp.webdriver.BitstampWebdriver;
import com.newpecunia.bitstamp.webdriver.impl.BitstampWebdriverImpl;
import com.newpecunia.net.HttpReaderFactory;
import com.newpecunia.net.HttpReaderFactoryImpl;
import com.newpecunia.synchronization.LockProvider;
import com.newpecunia.synchronization.SingleNodeLockProvider;

public class GuiceBitexchangeModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(HttpReaderFactory.class).to(HttpReaderFactoryImpl.class);
		bind(LockProvider.class).to(SingleNodeLockProvider.class);
		
		bind(BitstampService.class).to(BitstampServiceImpl.class);
		bind(BitstampCredentials.class).toInstance(SecureBitstampCredentialsImpl.newInstance());
		bind(BitstampWebdriver.class).to(BitstampWebdriverImpl.class);
	}

}
