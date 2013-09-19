package com.newpecunia.persistence.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.metamodel.MetadataSources;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class SessionFactoryBuilder {

	public static SessionFactory buildSessionFactory() {
		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().configure("/com/newpecunia/persistence/hibernate/hibernate.cfg.xml").buildServiceRegistry();
		MetadataSources metadataSources = new MetadataSources(serviceRegistry);
		return metadataSources.buildMetadata().buildSessionFactory();
	}
}
