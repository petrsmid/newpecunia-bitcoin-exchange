package com.newpecunia.unicredit.service.impl;

import java.math.BigDecimal;

import com.google.inject.Inject;
import com.newpecunia.ProgrammerException;
import com.newpecunia.configuration.NPConfiguration;
import com.newpecunia.time.TimeProvider;
import com.newpecunia.unicredit.service.BalanceService;
import com.newpecunia.unicredit.webdav.UnicreditWebdavService;

public class BalanceServiceImpl implements BalanceService {
		
	private BigDecimal balance = null;
	private long lastBalanceUpdate = 0;
	private TimeProvider timeProvider;
	private NPConfiguration configuration;
	private UnicreditWebdavService webdavService;

	@Inject
	public BalanceServiceImpl(UnicreditWebdavService webdavService, TimeProvider timeProvider, NPConfiguration configuration) {
		this.webdavService = webdavService;
		this.timeProvider = timeProvider;
		this.configuration = configuration;
	}
	
	@Override
	public synchronized BigDecimal getApproximateBalance() {
		long now = timeProvider.now();
		if (balance == null || (now - lastBalanceUpdate > configuration.getBalanceUpdatePeriod())) {
//			balance = webdavService....  TODO
		}
		return balance;
	}

	@Override
	public synchronized void substractFromBalance(BigDecimal amount, String currency) {
		String accountCurrency = configuration.getPayerAccountCurrency().toUpperCase();
		if (!currency.toUpperCase().equals(accountCurrency)) {
			throw new ProgrammerException("Cannot substract another currency as"+accountCurrency);
		}
		balance = balance.subtract(amount);		
	}

}
