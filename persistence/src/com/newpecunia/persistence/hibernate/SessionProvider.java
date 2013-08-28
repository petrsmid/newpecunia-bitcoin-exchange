package com.newpecunia.persistence.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.metamodel.MetadataSources;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import com.google.inject.Provider;

public class SessionProvider implements Provider<Session>{

	private SessionFactory sessionFactory;
	
	public SessionProvider() {
		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().configure("/com/newpecunia/persistence/hibernate/hibernate.cfg.xml").buildServiceRegistry();
		MetadataSources metadataSources = new MetadataSources(serviceRegistry);
		sessionFactory = metadataSources.buildMetadata().buildSessionFactory();
	}

	@Override
	public Session get() {
		return sessionFactory.getCurrentSession();
	}
}
