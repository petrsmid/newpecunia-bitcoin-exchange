package com.petrsmid.bitexchange;

import com.google.inject.AbstractModule;
import com.petrsmid.bitexchange.bitstamp.BitstampService;
import com.petrsmid.bitexchange.bitstamp.impl.BitstampCredentials;
import com.petrsmid.bitexchange.bitstamp.impl.BitstampServiceImpl;
import com.petrsmid.bitexchange.bitstamp.impl.SecureBitstampCredentialsImpl;
import com.petrsmid.bitexchange.net.HttpReader;
import com.petrsmid.bitexchange.net.HttpReaderImpl;

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
	}

}
