package com.newpecunia;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.newpecunia.countries.JavaCountryDatabaseTest;
import com.newpecunia.email.EmailSenderTest;
import com.newpecunia.net.JsonCodecTest;
import com.newpecunia.time.SKBusinessDayPlannerTest;


@RunWith(Suite.class)
@Suite.SuiteClasses({
	JavaCountryDatabaseTest.class,
	JsonCodecTest.class,
	EmailSenderTest.class,
	SKBusinessDayPlannerTest.class
})	
public class CommonTestSuite {

}
