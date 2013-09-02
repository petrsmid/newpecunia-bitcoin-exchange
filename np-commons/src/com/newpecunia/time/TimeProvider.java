package com.newpecunia.time;

import java.util.Calendar;

import org.joda.time.DateTime;

public interface TimeProvider {
	
	long now();

	Calendar nowCalendar();

	DateTime nowDateTime();
}
