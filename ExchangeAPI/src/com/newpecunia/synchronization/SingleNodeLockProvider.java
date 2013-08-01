package com.newpecunia.synchronization;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Provides locking across one JVM.
 */
public class SingleNodeLockProvider implements LockProvider {

	@Override
	public Lock getLock() {
		return new ReentrantLock();
	}

}
