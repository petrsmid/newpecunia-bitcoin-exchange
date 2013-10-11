package com.newpecunia;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.newpecunia.bitstamp.service.impl.BitstampServiceLiveTest;
import com.newpecunia.bitstamp.webdriver.BitstampWebdriverLiveTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	BitstampServiceLiveTest.class,
	BitstampWebdriverLiveTest.class
})	
public class LiveTestSuite {

}
