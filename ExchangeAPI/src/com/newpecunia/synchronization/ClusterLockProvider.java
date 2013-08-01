package com.newpecunia.synchronization;

import java.util.concurrent.locks.Lock;

public interface ClusterLockProvider {
	Lock getLock();
}
