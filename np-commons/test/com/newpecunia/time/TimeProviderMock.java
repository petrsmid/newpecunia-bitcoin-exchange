package com.newpecunia.time;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.joda.time.DateTime;

public class TimeProviderMock implements TimeProvider {

	private long time = 0;
	private Calendar cal = new GregorianCalendar(1970,0,1);
	private DateTime dateTime = new DateTime(0);
	
	@Override
	public long now() {
		return time;
	}
	
	@Override
	public Calendar nowCalendar() {
		return cal;
	}

	@Override
	public DateTime nowDateTime() {
		return dateTime;
	}


	public void setTime(long time) {
		this.time = time;
	}

	public void setCalendar(Calendar cal) {
		this.cal = cal;
	}

	public void setDateTime(DateTime dateTime) {
		this.dateTime = dateTime;
	}


	
	
}
