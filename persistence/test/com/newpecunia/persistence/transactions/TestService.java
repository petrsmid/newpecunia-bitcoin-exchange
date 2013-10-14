package com.newpecunia.persistence.transactions;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import com.google.inject.persist.UnitOfWork;
import com.newpecunia.persistence.entities.TestEntity;

@Singleton
public class TestService {

	public static final String METHOD1 = "method1";
	public static final String METHOD2 = "method2";
	
	@Inject
	private UnitOfWork unitOfWork;
	
	@Inject
	private Provider<EntityManager> emProvider;
	
	@Transactional
	public void transactionalMethod1(boolean failAfterSaving) {
		
		//save something
		emProvider.get().persist(new TestEntity(METHOD1));
		if (failAfterSaving) {
			throw new RuntimeException("Intentionally failing.");
		}
	}
	
	@Transactional
	public void transactionalMethod2(boolean failAfterSaving) {
		
		//save something
		emProvider.get().persist(new TestEntity(METHOD2));
		if (failAfterSaving) {
			throw new RuntimeException("Intentionally failing.");
		}
	}
	
	@Transactional
	public void transactionalMethod1And2(boolean fail1st, boolean fail2nd) {
		nonTransactionalMethod1And2(fail1st, fail2nd);
	}
	
	public void nonTransactionalMethod1And2(boolean fail1st, boolean fail2nd) {
		transactionalMethod1(fail1st);
		transactionalMethod2(fail2nd);
	}
	
	private List<TestEntity> listTestEntities(String text) {
		return emProvider.get().unwrap(Session.class).createCriteria(TestEntity.class).add(Restrictions.eq("text", text)).list();
	}
	
	public List<TestEntity> getEntitiesFromMethod1() {
		return listTestEntities(METHOD1);
	}
	
	public List<TestEntity> getEntitiesFromMethod2() {
		return listTestEntities(METHOD2);
	}

	@Transactional
	public void deleteAll() {
		emProvider.get().createQuery("delete from "+TestEntity.class.getName()+" where 1 = 1").executeUpdate();
	}	
	
}
