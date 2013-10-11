package com.newpecunia.time;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.joda.time.DateTime;

import com.google.inject.Singleton;

@Singleton
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
	
	@Override
	public Date nowDate() {
		return new Date();
	}

}
