package com.newpecunia.creditcard;

import com.google.inject.AbstractModule;
import com.newpecunia.creditcard.impl.CreditCardAcquiringServiceImpl;

public class CreditCardConnectorModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(CreditCardAcquiringService.class).to(CreditCardAcquiringServiceImpl.class);
		
	}


}
