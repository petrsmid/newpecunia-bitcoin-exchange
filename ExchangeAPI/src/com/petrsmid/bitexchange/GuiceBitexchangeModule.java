package com.petrsmid.bitexchange;

import com.google.inject.AbstractModule;
import com.petrsmid.bitexchange.bitstamp.TickerService;
import com.petrsmid.bitexchange.bitstamp.TickerServiceImpl;
import com.petrsmid.bitexchange.net.HttpReader;
import com.petrsmid.bitexchange.net.HttpReaderImpl;

public class GuiceBitexchangeModule extends AbstractModule {

	@Override
	protected void configure() {
		configureCommons();
		
		configuteBitstampService();
	}

	protected void configureCommons() {
		bind(HttpReader.class).to(HttpReaderImpl.class);
	}

	protected void configuteBitstampService() {
		bind(TickerService.class).to(TickerServiceImpl.class);
	}

}
