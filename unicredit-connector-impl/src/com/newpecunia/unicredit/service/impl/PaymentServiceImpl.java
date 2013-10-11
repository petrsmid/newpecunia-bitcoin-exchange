package com.newpecunia.unicredit.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import com.newpecunia.persistence.entities.ForeignPaymentOrder;
import com.newpecunia.persistence.entities.ForeignPaymentOrder.PaymentStatus;
import com.newpecunia.time.TimeProvider;
import com.newpecunia.unicredit.service.ForeignPayment;
import com.newpecunia.unicredit.service.PaymentService;
import com.newpecunia.unicredit.service.impl.entity.ForeignPaymentMapper;

@Singleton
public class PaymentServiceImpl implements PaymentService {
	
	private TimeProvider timeProvider;
	private ForeignPaymentMapper mapper;
	private Provider<EntityManager> entityManagerProvider;

	@Inject
	PaymentServiceImpl(Provider<EntityManager> enitityManagerProvider, TimeProvider timeProvider, ForeignPaymentMapper mapper) {		
		this.entityManagerProvider = enitityManagerProvider;
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
		
		getEntityManager().persist(paymentOrder);
	}
	
	@Override
	@Transactional
	public void createForeignPaymentOrder(ForeignPayment payment) {
		createOrder(payment, ForeignPaymentOrder.PaymentStatus.NEW, null);
	}

	@Override
	@Transactional
	public void createPreOrderWaitingForBTC(ForeignPayment preOrder, String acceptingBtcAddress) {
		createOrder(preOrder, ForeignPaymentOrder.PaymentStatus.WAITING_FOR_BTC, acceptingBtcAddress);
	}

	@Override
	@Transactional
	public void createOrderFromPreOrder(String receivingBtcAddress, BigDecimal amount) {
		Session session = getEntityManager().unwrap(Session.class);
		ForeignPaymentOrder preOrder = (ForeignPaymentOrder) session.createCriteria(ForeignPaymentOrder.class)
			.add(Restrictions.eq("status", PaymentStatus.WAITING_FOR_BTC))
			.add(Restrictions.eq("acceptingBtcAddress", receivingBtcAddress))
			.uniqueResult();
		
		preOrder.setAmount(amount);
		preOrder.setStatus(PaymentStatus.NEW);
		
		getEntityManager().persist(preOrder);
	}

	private EntityManager getEntityManager() {
		return entityManagerProvider.get();
	}
	


}
