package com.newpecunia.unicredit.service;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

import com.newpecunia.configuration.NPConfiguration;
import com.newpecunia.time.TimeProviderImpl;
import com.newpecunia.unicredit.service.impl.BalanceServiceImpl;
import com.newpecunia.unicredit.webdriver.impl.UnicreditWebdriverImpl;

public class BalanceServiceLiveTest {
	
	BalanceService service = new BalanceServiceImpl(new UnicreditWebdriverImpl(), new TimeProviderImpl(), new NPConfiguration());
	
	@Test
	public void getBalance() {
		BigDecimal balance = service.getApproximateBalance();
		Assert.assertNotNull(balance);
	}

}
