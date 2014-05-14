package com.newpecunia;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.newpecunia.bitstamp.service.impl.BitstampServiceLiveTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	BitstampServiceLiveTest.class,
})	
public class LiveTestSuite {

}
