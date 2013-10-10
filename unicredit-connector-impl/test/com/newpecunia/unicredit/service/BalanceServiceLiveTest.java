package com.newpecunia.unicredit.service;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

public class BalanceServiceLiveTest {
	
	BalanceService service = null;
	
	@Test
	public void getBalance() {
		BigDecimal balance = service.getApproximateBalance();
		Assert.assertNotNull(balance);
	}

}
