package com.newpecunia;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.newpecunia.bitstamp.service.BitstampServiceLiveTest;
import com.newpecunia.bitstamp.webdriver.BitstampWebdriverLiveTest;
import com.newpecunia.net.HttpReaderLiveTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	HttpReaderLiveTest.class,
	BitstampServiceLiveTest.class,
	BitstampWebdriverLiveTest.class
})	
public class LiveTestSuite {

}
