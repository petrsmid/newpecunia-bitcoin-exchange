package com.newpecunia.persistence;

import org.hibernate.Session;

import com.google.inject.AbstractModule;
import com.newpecunia.persistence.hibernate.SessionProvider;


public class PersistenceModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Session.class).toProvider(SessionProvider.class);
	}

}
