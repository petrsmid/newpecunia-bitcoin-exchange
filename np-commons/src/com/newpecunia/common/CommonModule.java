package com.newpecunia.common;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import com.google.inject.AbstractModule;
import com.newpecunia.countries.CountryDatabase;
import com.newpecunia.countries.JavaCountryDatabase;
import com.newpecunia.time.TimeProvider;
import com.newpecunia.time.TimeProviderImpl;

public class CommonModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(CountryDatabase.class).to(JavaCountryDatabase.class);
		bind(TimeProvider.class).to(TimeProviderImpl.class);
		bind(MapperFactory.class).toInstance(new DefaultMapperFactory.Builder().build());
	}

}
