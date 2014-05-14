package com.newpecunia;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.newpecunia.countries.JavaCountryDatabaseTest;
import com.newpecunia.email.EmailSenderTest;
import com.newpecunia.logging.ThrottlingSMTPAppenderTest;
import com.newpecunia.net.JsonCodecTest;


@RunWith(Suite.class)
@Suite.SuiteClasses({
	JavaCountryDatabaseTest.class,
	JsonCodecTest.class,
	EmailSenderTest.class,
	ThrottlingSMTPAppenderTest.class
})	
public class CommonTestSuite {

}
