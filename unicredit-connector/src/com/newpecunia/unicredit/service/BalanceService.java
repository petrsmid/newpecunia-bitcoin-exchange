package com.newpecunia.unicredit.service;

import java.math.BigDecimal;

public interface BalanceService {
	
	BigDecimal getApproximateBalance();
	
	void substractFromBalance(BigDecimal amount, String currency);

}
