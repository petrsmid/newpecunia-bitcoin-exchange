package com.newpecunia.unicredit.service.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.google.inject.Inject;
import com.newpecunia.unicredit.service.ForeignPayment;
import com.newpecunia.unicredit.service.PaymentService;
import com.newpecunia.unicredit.service.entity.ForeignPaymentOrder;

public class PaymentServiceImpl implements PaymentService {
	
	private Session session;

	@Inject
	public PaymentServiceImpl(Session session) {
		this.session = session;
	}
	
	@Override
	public void createForeignPaymentOrder(ForeignPayment payment) {
		ForeignPaymentOrder paymentOrder = new ForeignPaymentOrder();
		//TODO map payment -> paymentOrder
		Transaction transaction = session.beginTransaction();		
		session.save(paymentOrder);
		transaction.commit();
	}

}
