package com.newpecunia.persistence;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import com.newpecunia.persistence.entities.LastShortId;

/**
 * Generates sequentially Integer ID. If the ID is bigger than Integer.MAX_VALUE it continues with Integer.MIN_VALUE.
 * It uses one table and SELECT ... FOR UPDATE semantics to prevent creating duplicate IDs in concurrent environment.
 * Reimplement in environment with huge load. (E.g. use Hi/Lo generator instead)  
 */
@Singleton
public class ShortCyclicIdGenerator {
	
	private Provider<EntityManager> emProvider;
	
	@Inject
	ShortCyclicIdGenerator(Provider<EntityManager> emProvider) {
		this.emProvider = emProvider;
	}
	
	@Transactional
	public Integer acquireNextShorCyclicId() {
		EntityManager em = emProvider.get();
		LastShortId idEntity = em.find(LastShortId.class, LastShortId.CONSTANT_ID, LockModeType.PESSIMISTIC_WRITE);
		int lastId = idEntity.getId();
		idEntity.setId(++lastId);
		em.persist(idEntity);
		return lastId;
	}

}
