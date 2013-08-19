package com.newpecunia.util;

import com.newpecunia.util.TimeProvider;

public class TimeProviderMock implements TimeProvider {

	long time = 0;
	
	@Override
	public long now() {
		return time;
	}
	
	public void setTime(long time) {
		this.time = time;
	}

}
