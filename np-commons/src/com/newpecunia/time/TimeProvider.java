package com.newpecunia.time;

import java.util.Calendar;
import java.util.Date;

import org.joda.time.DateTime;

public interface TimeProvider {
	
	long now();

	Calendar nowCalendar();

	DateTime nowDateTime();
	
	Date nowDate();
}
