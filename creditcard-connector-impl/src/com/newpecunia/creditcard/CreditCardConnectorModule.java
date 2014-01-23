package com.newpecunia.creditcard;

import com.google.inject.AbstractModule;
import com.newpecunia.creditcard.impl.CreditCardAcquiringUnicreditServiceImpl;

public class CreditCardConnectorModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(CreditCardAcquiringService.class).to(CreditCardAcquiringUnicreditServiceImpl.class);
		
	}


}
