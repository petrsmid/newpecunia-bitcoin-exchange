package com.newpecunia;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.newpecunia.bitstamp.service.impl.BitstampServiceTest;
import com.newpecunia.bitstamp.service.impl.dto.OrderMapperTest;
import com.newpecunia.net.BitstampRequestCountLimitVerifierTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	BitstampServiceTest.class,
	OrderMapperTest.class,
	BitstampRequestCountLimitVerifierTest.class,
})	
public class CommonTestSuite {

}
