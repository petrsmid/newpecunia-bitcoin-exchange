package com.newpecunia;

import com.google.inject.AbstractModule;
import com.newpecunia.bitstamp.service.BitstampService;
import com.newpecunia.bitstamp.service.impl.BitstampCredentials;
import com.newpecunia.bitstamp.service.impl.BitstampServiceImpl;
import com.newpecunia.bitstamp.service.impl.SecureBitstampCredentialsImpl;
import com.newpecunia.bitstamp.webdriver.BitstampWebdriver;
import com.newpecunia.bitstamp.webdriver.impl.BitstampWebdriverImpl;
import com.newpecunia.net.HttpReader;
import com.newpecunia.net.HttpReaderImpl;

public class GuiceBitexchangeModule extends AbstractModule {

	@Override
	protected void configure() {
		configureCommons();
		
		configureBitstamp();
	}

	protected void configureCommons() {
		bind(HttpReader.class).to(HttpReaderImpl.class);
	}

	private void configureBitstamp() {
		bind(BitstampService.class).to(BitstampServiceImpl.class);
		bind(BitstampCredentials.class).toInstance(SecureBitstampCredentialsImpl.newInstance());
		bind(BitstampWebdriver.class).to(BitstampWebdriverImpl.class);
	}

}
