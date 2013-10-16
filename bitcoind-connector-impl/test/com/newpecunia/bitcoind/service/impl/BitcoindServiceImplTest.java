package com.newpecunia.bitcoind.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import junit.framework.Assert;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.newpecunia.bitcoind.BitcoindConnectorModule;
import com.newpecunia.bitcoind.service.BitcoindService;
import com.newpecunia.common.CommonModule;
import com.newpecunia.persistence.entities.ReceivingBitcoinAddressStatus;
import com.newpecunia.persistence.entities.ReceivingBitcoinAddressStatus.AddressStatus;

public class BitcoindServiceImplTest {
	
	private BitcoindService service;
	private PersistService persistService;
	private Injector injector;

	@Before
	public void setup() {
		injector = Guice.createInjector(new JpaPersistModule("testingJpaUnit"), new BitcoindConnectorModule(), new CommonModule());
		service = injector.getInstance(BitcoindService.class);
		persistService  = injector.getInstance(PersistService.class);
		persistService.start();
		
		deleteAll();
		generate1000BtcAddresses();
	}
	
	private void generate1000BtcAddresses() {
		Provider<EntityManager> emProvider = injector.getProvider(EntityManager.class);
		EntityTransaction tx = emProvider.get().getTransaction();
		tx.begin();
		for(int i = 0; i < 1000; i++) {
			ReceivingBitcoinAddressStatus addrStatus = new ReceivingBitcoinAddressStatus();
			addrStatus.setAddress("addr"+i);
			addrStatus.setStatus(AddressStatus.FREE);
			emProvider.get().persist(addrStatus);
		}
		tx.commit();		
	}
	
	private void deleteAll() {
		Provider<EntityManager> emProvider = injector.getProvider(EntityManager.class);
		EntityTransaction tx = emProvider.get().getTransaction();
		tx.begin();
		emProvider.get().createQuery("delete from "+ReceivingBitcoinAddressStatus.class.getName()).executeUpdate();
		tx.commit();		
	}

	@After
	public void tearDown() {
		deleteAll();
		persistService.stop();
	}
	
	@Test
	public void testAcquireBtcAddress() {
		String address = service.acquireAddressForReceivingBTC();
		Assert.assertNotNull(address);
		Assert.assertEquals(AddressStatus.USED, getAddrStatus(address).getStatus());
	}
	
	@Test
	public void testConcurrendAcquire() throws InterruptedException {
		int numThreads = 100;

	    final CountDownLatch counter = new CountDownLatch(numThreads);
		final Set<String> acquiredAddresses = Collections.synchronizedSet(new HashSet<String>());
		final List<String> duplicates = Collections.synchronizedList(new ArrayList<String>());
	    List<Thread> threads = new ArrayList<Thread>();
	    
	    //prepare threads
		for (int i = 0; i < numThreads; i++) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					String newAddress = service.acquireAddressForReceivingBTC();
					if (acquiredAddresses.contains(newAddress)) {
						duplicates.add(newAddress);
					} else {
						acquiredAddresses.add(newAddress);
					}
					counter.countDown();
				}
			});
			threads.add(t);
		}
		
		//start the threads
		for (Thread thread : threads) {
			thread.start();
		}
	    counter.await();
	    
	    Assert.assertTrue(duplicates.isEmpty());
	    Assert.assertEquals(numThreads, acquiredAddresses.size());
	    List<String> theAddresses = new ArrayList<>(acquiredAddresses);
	    Collections.sort(theAddresses);
	    for (String addr : theAddresses) {
			System.out.println(addr);
		}
	}
	
	@Test
	public void testAcquireAndRelease() {
		String address = service.acquireAddressForReceivingBTC();
		Assert.assertNotNull(address);
		Assert.assertEquals(AddressStatus.USED, getAddrStatus(address).getStatus());

		service.releaseAddressForReceivingBTC(address);
		
		//test whether it is in status FREE
		Assert.assertEquals(AddressStatus.FREE, getAddrStatus(address).getStatus());
	}
	
	private ReceivingBitcoinAddressStatus getAddrStatus(String address) {
		Provider<EntityManager> emProvider = injector.getProvider(EntityManager.class);
		return (ReceivingBitcoinAddressStatus) emProvider.get().unwrap(Session.class).createCriteria(ReceivingBitcoinAddressStatus.class)
				.add(Restrictions.eq("address", address))
				.uniqueResult();
	}
}
