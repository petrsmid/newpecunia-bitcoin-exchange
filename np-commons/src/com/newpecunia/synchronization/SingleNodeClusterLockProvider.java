package com.newpecunia.synchronization;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Provides locking across only one JVM.
 */
public class SingleNodeClusterLockProvider implements ClusterLockProvider {

	@Override
	public Lock getLock() {
		return new ReentrantLock();
	}

}
