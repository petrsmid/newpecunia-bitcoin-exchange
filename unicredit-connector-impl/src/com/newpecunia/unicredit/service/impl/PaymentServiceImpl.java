package com.newpecunia.unicredit.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.google.inject.Inject;
import com.newpecunia.persistence.entities.ForeignPaymentOrder;
import com.newpecunia.persistence.entities.ForeignPaymentOrder.PaymentStatus;
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
	

	private void createOrder(ForeignPayment payment, ForeignPaymentOrder.PaymentStatus status, String acceptingBtcAddress) {
		ForeignPaymentOrder paymentOrder = mapper.mapToOrder(payment);
		Calendar now = timeProvider.nowCalendar();
		paymentOrder.setAcceptingBtcAddress(acceptingBtcAddress);
		paymentOrder.setStatus(status);
		paymentOrder.setCreateTimestamp(now);
		paymentOrder.setUpdateTimestamp(now);
		
		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.beginTransaction();		
		session.save(paymentOrder);
		session.flush();
		transaction.commit();
		session.clear();
		
	}
	
	@Override
	public void createForeignPaymentOrder(ForeignPayment payment) {
		createOrder(payment, ForeignPaymentOrder.PaymentStatus.NEW, null);
	}

	@Override
	public void createPreOrderWaitingForBTC(ForeignPayment preOrder, String acceptingBtcAddress) {
		createOrder(preOrder, ForeignPaymentOrder.PaymentStatus.WAITING_FOR_BTC, acceptingBtcAddress);
	}

	@Override
	public void createOrderFromPreOrder(String receivingBtcAddress, BigDecimal amount) {
		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.beginTransaction();		
		ForeignPaymentOrder preOrder = (ForeignPaymentOrder) session.createCriteria(ForeignPaymentOrder.class)
			.add(Restrictions.eq("status", PaymentStatus.WAITING_FOR_BTC))
			.add(Restrictions.eq("acceptingBtcAddress", receivingBtcAddress))
			.uniqueResult();
		
		preOrder.setAmount(amount);
		preOrder.setStatus(PaymentStatus.NEW);
		
		session.update(preOrder);
		
		session.flush();
		transaction.commit();
		session.clear();
	}

	


}
