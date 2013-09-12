package com.newpecunia;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.newpecunia.bitstamp.BitstampRequestCountLimitVerifierTest;
import com.newpecunia.bitstamp.service.BitstampServiceTest;
import com.newpecunia.bitstamp.service.impl.dto.OrderMapperTest;
import com.newpecunia.net.JsonCodecTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	BitstampServiceTest.class,
	OrderMapperTest.class,
	JsonCodecTest.class,
	BitstampRequestCountLimitVerifierTest.class,
})	
public class CommonTestSuite {

}
