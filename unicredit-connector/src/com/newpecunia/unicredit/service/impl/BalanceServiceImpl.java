package com.newpecunia.unicredit.service.impl;

import java.math.BigDecimal;

import com.google.inject.Inject;
import com.newpecunia.ProgrammerException;
import com.newpecunia.configuration.NPConfiguration;
import com.newpecunia.time.TimeProvider;
import com.newpecunia.unicredit.service.BalanceService;
import com.newpecunia.unicredit.webdriver.UnicreditWebdriver;

public class BalanceServiceImpl implements BalanceService {
		
	private UnicreditWebdriver webdriver;
	private BigDecimal balance = null;
	private long lastBalanceUpdate = 0;
	private TimeProvider timeProvider;

	@Inject
	public BalanceServiceImpl(UnicreditWebdriver webdriver, TimeProvider timeProvider) {
		this.webdriver = webdriver;
		this.timeProvider = timeProvider;
	}
	
	@Override
	public synchronized BigDecimal getApproximateBalance() {
		long now = timeProvider.now();
		if (balance == null || (now - lastBalanceUpdate > NPConfiguration.INSTANCE.getBalanceUpdatePeriad())) {
			balance = webdriver.getBalance();
		}
		return balance;
	}

	@Override
	public synchronized void substractFromBalance(BigDecimal amount, String currency) {
		String accountCurrency = NPConfiguration.INSTANCE.getUnicreditAccountCurrency().toUpperCase();
		if (!currency.toUpperCase().equals(accountCurrency)) {
			throw new ProgrammerException("Cannot substract another currency as"+accountCurrency);
		}
		balance = balance.subtract(amount);		
	}

}
