package com.newpecunia.synchronization;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Provides locking across only one JVM.
 */
public class SingleJVMLockProvider implements LockProvider {

	@Override
	public Lock getLock() {
		return new ReentrantLock();
	}

}
