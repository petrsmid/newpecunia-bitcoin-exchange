package com.newpecunia;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.newpecunia.unicredit.service.impl.BalanceServiceTest;
import com.newpecunia.unicredit.service.impl.entity.ForeignPaymentMapperTest;
import com.newpecunia.unicredit.service.impl.processor.PreorderCleanerJobTest;
import com.newpecunia.unicredit.webdav.impl.StatusImplTest;
import com.newpecunia.unicredit.webdav.impl.StructuredMulticashStatementParserTest;
import com.newpecunia.unicredit.webdav.impl.UnicreditFileNameResolverTest;


@RunWith(Suite.class)
@Suite.SuiteClasses({
	BalanceServiceTest.class,
	ForeignPaymentMapperTest.class,
	PreorderCleanerJobTest.class,
	UnicreditFileNameResolverTest.class,
	StatusImplTest.class,
	StructuredMulticashStatementParserTest.class,
	UnicreditFileNameResolverTest.class
})	
public class CommonTestSuite {

}
