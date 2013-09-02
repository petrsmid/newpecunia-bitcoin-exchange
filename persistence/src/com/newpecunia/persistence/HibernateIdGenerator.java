package com.newpecunia.persistence;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

public class HibernateIdGenerator implements IdentifierGenerator {

	
	@Override
	public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
		return IdGenerator.INSTANCE.nextId();
	}

}
