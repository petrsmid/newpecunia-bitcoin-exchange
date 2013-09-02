package com.newpecunia.unicredit.service.impl;

import java.util.Calendar;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.google.inject.Inject;
import com.newpecunia.time.TimeProvider;
import com.newpecunia.unicredit.service.ForeignPayment;
import com.newpecunia.unicredit.service.PaymentService;
import com.newpecunia.unicredit.service.impl.entity.ForeignPaymentMapper;
import com.newpecunia.unicredit.service.impl.entity.ForeignPaymentOrder;

public class PaymentServiceImpl implements PaymentService {
	
	private Session session;
	private TimeProvider timeProvider;
	private ForeignPaymentMapper mapper;

	@Inject
	public PaymentServiceImpl(Session session, TimeProvider timeProvider, ForeignPaymentMapper mapper) {		
		this.session = session;
		this.timeProvider = timeProvider;
		this.mapper = mapper;
	}
	
	@Override
	public void createForeignPaymentOrder(ForeignPayment payment) {
		ForeignPaymentOrder paymentOrder = mapper.mapToOrder(payment);
		Calendar now = timeProvider.nowCalendar();
		paymentOrder.setStatus(ForeignPaymentOrder.PaymentStatus.NEW);
		paymentOrder.setCreateTimestamp(now);
		paymentOrder.setUpdateTimestamp(now);
		
		Transaction transaction = session.beginTransaction();		
		session.save(paymentOrder);
		session.flush();
		transaction.commit();
		session.clear();
	}

}
