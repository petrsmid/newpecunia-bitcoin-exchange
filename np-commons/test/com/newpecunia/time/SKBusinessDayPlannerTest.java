package com.newpecunia.time;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Assert;
import org.junit.Test;

public class SKBusinessDayPlannerTest {
	
	@Test
	public void testEasterForward() {
		SKBusinessDayPlanner planner = new SKBusinessDayPlanner();
		Calendar tuesdayAfterEaster = planner.moveByBusinessDays(new GregorianCalendar(2013, 2, 28), 1);
		//assert 2013-04-02
		Assert.assertEquals(2013, tuesdayAfterEaster.get(Calendar.YEAR));
		Assert.assertEquals(3, tuesdayAfterEaster.get(Calendar.MONTH)); //april
		Assert.assertEquals(2, tuesdayAfterEaster.get(Calendar.DAY_OF_MONTH));
	}

	
	@Test
	public void testEasterBackward() {
		SKBusinessDayPlanner planner = new SKBusinessDayPlanner();
		Calendar tuesdayAfterEaster = planner.moveByBusinessDays(new GregorianCalendar(2013, 3, 02), -1);
		//assert 2013-03-28
		Assert.assertEquals(2013, tuesdayAfterEaster.get(Calendar.YEAR));
		Assert.assertEquals(2, tuesdayAfterEaster.get(Calendar.MONTH)); //march
		Assert.assertEquals(28, tuesdayAfterEaster.get(Calendar.DAY_OF_MONTH));
	}
	
}
