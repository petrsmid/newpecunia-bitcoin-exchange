package com.newpecunia.common;

import com.google.inject.AbstractModule;
import com.newpecunia.countries.CountryDatabase;
import com.newpecunia.countries.JavaCountryDatabase;

public class CommonModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(CountryDatabase.class).to(JavaCountryDatabase.class);
	}

}
