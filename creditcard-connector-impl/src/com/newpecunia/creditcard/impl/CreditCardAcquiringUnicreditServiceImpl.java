package com.newpecunia.creditcard.impl;

import java.math.BigDecimal;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.newpecunia.creditcard.CreditCardAcquiringService;

@Singleton
public class CreditCardAcquiringUnicreditServiceImpl implements CreditCardAcquiringService {

	@Inject
	CreditCardAcquiringUnicreditServiceImpl() {
	}

	@Override
	public String startCardPaymentTransaction(BigDecimal amount) {
		// TODO implement me
		return null;
	}

	@Override
	public TransactionStatus getTransactionStatus(String txId) {
		// TODO implement me
		return null;
	}

	@Override
	public CancelStatus cancelTransaction(String txId) {
		// TODO implement me
		return null;
	}

	@Override
	public CloseDayStatus closeDay() {
		// TODO implement me
		return null;
	}
	
	
}
