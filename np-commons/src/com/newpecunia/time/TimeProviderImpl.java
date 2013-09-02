package com.newpecunia.time;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.joda.time.DateTime;

public class TimeProviderImpl implements TimeProvider {

	@Override
	public long now() {
		return System.currentTimeMillis();
	}
	
	@Override
	public Calendar nowCalendar() {
		return new GregorianCalendar();
	}
	
	@Override
	public DateTime nowDateTime() {
		return new DateTime();
	}

}
