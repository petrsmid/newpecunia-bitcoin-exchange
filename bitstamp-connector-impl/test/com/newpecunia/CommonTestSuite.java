package com.newpecunia;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.newpecunia.bitstamp.service.impl.BitstampServiceTest;
import com.newpecunia.bitstamp.service.impl.dto.OrderMapperTest;
import com.newpecunia.bitstamp.service.impl.net.BitstampRequestCountLimitVerifierTest;
import com.newpecunia.bitstamp.service.impl.net.HttpReaderLiveTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	BitstampServiceTest.class,
	OrderMapperTest.class,
	BitstampRequestCountLimitVerifierTest.class,
	HttpReaderLiveTest.class,
	BitstampRequestCountLimitVerifierTest.class
})	
public class CommonTestSuite {

}
