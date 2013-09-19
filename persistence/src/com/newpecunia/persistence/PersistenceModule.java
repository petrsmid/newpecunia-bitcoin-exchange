package com.newpecunia.persistence;

import org.hibernate.SessionFactory;

import com.google.inject.AbstractModule;
import com.newpecunia.persistence.hibernate.SessionFactoryBuilder;


public class PersistenceModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(SessionFactory.class).toInstance(SessionFactoryBuilder.buildSessionFactory());
	}

}
