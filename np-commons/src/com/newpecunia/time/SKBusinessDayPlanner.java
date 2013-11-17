package com.newpecunia.time;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class SKBusinessDayPlanner {

	private static final int START_YEAR = 2000;
	private static final int END_YEAR = 2500;

	private Set<LocalDate> holidays = new HashSet<LocalDate>();
	

	@Inject
	SKBusinessDayPlanner() {
		for (int year = START_YEAR; year < END_YEAR; year++) {
			holidays.add(new LocalDate(year, 1, 1));
			holidays.add(new LocalDate(year, 1, 6));
			holidays.add(getEasterFriday(year));
			holidays.add(getEasterMonday(year));
			holidays.add(new LocalDate(year, 5, 1));
			holidays.add(new LocalDate(year, 5, 8));
			holidays.add(new LocalDate(year, 7, 5));
			holidays.add(new LocalDate(year, 8,29));
			holidays.add(new LocalDate(year, 9, 1));
			holidays.add(new LocalDate(year, 9,15));
			holidays.add(new LocalDate(year,11, 1));
			holidays.add(new LocalDate(year,11,17));
			holidays.add(new LocalDate(year,12,24));
			holidays.add(new LocalDate(year,12,25));
			holidays.add(new LocalDate(year,12,26));
		}
	}
	
	
	public Calendar moveByBusinessDays(Calendar day, int days) {
		Calendar tmpCal = (Calendar) day.clone();
		int step = days < 0 ? -1 : 1;
		for (int i = 0; i < Math.abs(days); i++) {
			tmpCal.add(Calendar.DAY_OF_MONTH, step);
			//skip holiday days
			while (Calendar.SATURDAY == tmpCal.get(Calendar.DAY_OF_WEEK)
					|| Calendar.SUNDAY == tmpCal.get(Calendar.DAY_OF_WEEK)
					|| isHoliday(tmpCal)) {
				
				tmpCal.add(Calendar.DAY_OF_MONTH, step);
			}
		}
		
		return tmpCal;
	}
	
	private boolean isHoliday(Calendar cal) {
		LocalDate tmpDate = new LocalDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) +1, cal.get(Calendar.DAY_OF_MONTH));
		return (holidays.contains(tmpDate));
	}
	
	
	private LocalDate getEasterMonday(int year) {
		GregorianCalendar willBeMonday = getEasterSunday(year);
		willBeMonday.add(Calendar.DATE, 1);
		return new LocalDate(willBeMonday.get(Calendar.YEAR), willBeMonday.get(Calendar.MONTH)+1, willBeMonday.get(Calendar.DAY_OF_MONTH));
	}
	
	private LocalDate getEasterFriday(int year) {
		GregorianCalendar wasFriday = getEasterSunday(year);
		wasFriday.add(Calendar.DATE, -2);
		return new LocalDate(wasFriday.get(Calendar.YEAR), wasFriday.get(Calendar.MONTH)+1, wasFriday.get(Calendar.DAY_OF_MONTH));
	}
	
	/**
	 * From http://www.javaworld.com/javatips/javatip44/Holidays.java   
	 */
	private GregorianCalendar getEasterSunday(int year) {
		/*
		 * Calculate Easter Sunday
		 * 
		 * Written by Gregory N. Mirsky
		 * 
		 * Source: 2nd Edition by Peter Duffett-Smith. It was originally from
		 * Butcher's Ecclesiastical Calendar, published in 1876. This algorithm
		 * has also been published in the 1922 book General Astronomy by Spencer
		 * Jones; in The Journal of the British Astronomical Association
		 * (Vol.88, page 91, December 1977); and in Astronomical Algorithms
		 * (1991) by Jean Meeus.
		 * 
		 * This algorithm holds for any year in the Gregorian Calendar, which
		 * (of course) means years including and after 1583.
		 * 
		 * a=year%19 b=year/100 c=year%100 d=b/4 e=b%4 f=(b+8)/25 g=(b-f+1)/3
		 * h=(19*a+b-d-g+15)%30 i=c/4 k=c%4 l=(32+2*e+2*i-h-k)%7
		 * m=(a+11*h+22*l)/451 Easter Month =(h+l-7*m+114)/31 [3=March, 4=April]
		 * p=(h+l-7*m+114)%31 Easter Date=p+1 (date in Easter Month)
		 * 
		 * Note: Integer truncation is already factored into the calculations.
		 * Using higher percision variables will cause inaccurate calculations.
		 */

		int nA = 0;
		int nB = 0;
		int nC = 0;
		int nD = 0;
		int nE = 0;
		int nF = 0;
		int nG = 0;
		int nH = 0;
		int nI = 0;
		int nK = 0;
		int nL = 0;
		int nM = 0;
		int nP = 0;
		int nEasterMonth = 0;
		int nEasterDay = 0;

		// Calculate Easter
		nA = year % 19;
		nB = year / 100;
		nC = year % 100;
		nD = nB / 4;
		nE = nB % 4;
		nF = (nB + 8) / 25;
		nG = (nB - nF + 1) / 3;
		nH = (19 * nA + nB - nD - nG + 15) % 30;
		nI = nC / 4;
		nK = nC % 4;
		nL = (32 + 2 * nE + 2 * nI - nH - nK) % 7;
		nM = (nA + 11 * nH + 22 * nL) / 451;

		// [3=March, 4=April]
		nEasterMonth = (nH + nL - 7 * nM + 114) / 31;
		--nEasterMonth;
		nP = (nH + nL - 7 * nM + 114) % 31;

		// Date in Easter Month.
		nEasterDay = nP + 1;

		// Populate the calendar object...
		return new GregorianCalendar(year, nEasterMonth, nEasterDay);
	} 
}
