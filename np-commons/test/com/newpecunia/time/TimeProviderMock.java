package com.newpecunia.time;

import java.util.Calendar;

import org.joda.time.DateTime;

import com.newpecunia.time.TimeProvider;

public class TimeProviderMock implements TimeProvider {

	long time = 0;
	
	@Override
	public long now() {
		return time;
	}
	
	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public Calendar nowCalendar() {
		return null;
	}

	@Override
	public DateTime nowDateTime() {
		return null;
	}

}
