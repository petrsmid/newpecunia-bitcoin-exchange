package com.newpecunia.unicredit.service;

import java.math.BigDecimal;

public interface PaymentService {

	void createForeignPaymentOrder(ForeignPayment paymentOrder);
	void createPreOrderWaitingForBTC(ForeignPayment preOrder, String acceptingBtcAddress);
	void createOrderFromPreOrder(String receivingBtcAddress, BigDecimal amount);

}
