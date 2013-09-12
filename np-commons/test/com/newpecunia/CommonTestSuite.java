package com.newpecunia;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.newpecunia.countries.JavaCountryDatabaseTest;


@RunWith(Suite.class)
@Suite.SuiteClasses({
	JavaCountryDatabaseTest.class,
})	
public class CommonTestSuite {

}
