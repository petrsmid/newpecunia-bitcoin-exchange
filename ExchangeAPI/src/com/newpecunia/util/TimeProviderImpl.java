package com.newpecunia.util;

public class TimeProviderImpl implements TimeProvider {

	@Override
	public long now() {
		return System.currentTimeMillis();
	}

}
