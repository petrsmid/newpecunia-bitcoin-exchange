package com.newpecunia;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.newpecunia.unicredit.service.impl.entity.ForeignPaymentMapperTest;
import com.newpecunia.unicredit.webdav.impl.UnicreditFileNameResolverTest;


@RunWith(Suite.class)
@Suite.SuiteClasses({
	ForeignPaymentMapperTest.class,
	UnicreditFileNameResolverTest.class
})	
public class CommonTestSuite {

}
