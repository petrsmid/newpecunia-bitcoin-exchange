package com.newpecunia.unicredit.service.impl;

import java.util.Calendar;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.google.inject.Inject;
import com.newpecunia.persistence.entities.ForeignPaymentOrder;
import com.newpecunia.time.TimeProvider;
import com.newpecunia.unicredit.service.ForeignPayment;
import com.newpecunia.unicredit.service.PaymentService;
import com.newpecunia.unicredit.service.impl.entity.ForeignPaymentMapper;

public class PaymentServiceImpl implements PaymentService {
	
	private SessionFactory sessionFactory;
	private TimeProvider timeProvider;
	private ForeignPaymentMapper mapper;

	@Inject
	public PaymentServiceImpl(SessionFactory sessionFactory, TimeProvider timeProvider, ForeignPaymentMapper mapper) {		
		this.sessionFactory = sessionFactory;
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
		
		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.beginTransaction();		
		session.save(paymentOrder);
		session.flush();
		transaction.commit();
		session.clear();
	}

}
