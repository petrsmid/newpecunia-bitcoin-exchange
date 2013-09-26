package com.newpecunia;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.newpecunia.countries.JavaCountryDatabaseTest;
import com.newpecunia.net.HttpReaderLiveTest;
import com.newpecunia.net.JsonCodecTest;


@RunWith(Suite.class)
@Suite.SuiteClasses({
	JavaCountryDatabaseTest.class,
	HttpReaderLiveTest.class,
	JsonCodecTest.class,	
})	
public class CommonTestSuite {

}
