package com.newpecunia.synchronization;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.google.inject.Singleton;

/**
 * Provides locking across only one JVM.
 */
@Singleton
public class SingleJVMLockProvider implements LockProvider {

	SingleJVMLockProvider(){}
	
	@Override
	public Lock getLock() {
		return new ReentrantLock();
	}

}
