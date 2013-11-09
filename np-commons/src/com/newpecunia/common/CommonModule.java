package com.newpecunia.common;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import com.google.inject.AbstractModule;
import com.newpecunia.countries.CountryDatabase;
import com.newpecunia.countries.JavaCountryDatabase;
import com.newpecunia.email.EmailSender;
import com.newpecunia.email.EmailSenderImpl;
import com.newpecunia.synchronization.LockProvider;
import com.newpecunia.synchronization.SingleJVMLockProvider;
import com.newpecunia.time.TimeProvider;
import com.newpecunia.time.TimeProviderImpl;

public class CommonModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(LockProvider.class).to(SingleJVMLockProvider.class);
		bind(CountryDatabase.class).to(JavaCountryDatabase.class);
		bind(TimeProvider.class).to(TimeProviderImpl.class);
		bind(MapperFactory.class).toInstance(new DefaultMapperFactory.Builder().build());
		bind(EmailSender.class).to(EmailSenderImpl.class);
	}

}
