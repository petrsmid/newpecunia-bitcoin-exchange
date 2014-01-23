package com.newpecunia.creditcard;

import java.math.BigDecimal;

public interface CreditCardAcquiringService {

	public enum TransactionStatus {OK, ERROR_FAILED, ERROR_DECLINED, ERROR_REVERSED, ERROR_TIMEOUT, ERROR_PENDING } 
	public enum CancelStatus { OK, FAILED, REVERSED }
	public enum CloseDayStatus { OK, FAILED }
	
	String startCardPaymentTransaction(BigDecimal amount);
	
	TransactionStatus getTransactionStatus(String txId);
	
	CancelStatus cancelTransaction(String txId);
	
	CloseDayStatus closeDay();
	
}
