package com.newpecunia.unicredit.service.impl;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.Calendar;

import javax.persistence.EntityManager;

import org.apache.commons.codec.binary.Base32;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import com.newpecunia.persistence.ShortCyclicIdGenerator;
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
	private ShortCyclicIdGenerator shortRefGenerator;

	@Inject
	PaymentServiceImpl(Provider<EntityManager> enitityManagerProvider, TimeProvider timeProvider, ForeignPaymentMapper mapper, ShortCyclicIdGenerator shortRefGenerator) {		
		this.entityManagerProvider = enitityManagerProvider;
		this.timeProvider = timeProvider;
		this.mapper = mapper;
		this.shortRefGenerator = shortRefGenerator;
	}
	

	private ForeignPaymentOrder createOrderFromPayment(ForeignPayment payment, ForeignPaymentOrder.PaymentStatus status, String acceptingBtcAddress) {
		ForeignPaymentOrder paymentOrder = mapper.mapToOrder(payment);
		Calendar now = timeProvider.nowCalendar();
//		paymentOrder.setRequestorEmail() TODO - set e-mail of the requestor
		paymentOrder.setAcceptingBtcAddress(acceptingBtcAddress);
		paymentOrder.setStatus(status);
		paymentOrder.setCreateTimestamp(now);
		paymentOrder.setUpdateTimestamp(now);
		String shortReference = convertToBase32(shortRefGenerator.acquireNextShorCyclicId());
		paymentOrder.setShortUnicreditReference(shortReference);
		
		return paymentOrder;
	}
	
	private String convertToBase32(int shortId) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(4);		
		byteBuffer.putInt(shortId);
		Base32 base32 = new Base32(); //we do not use base64 because it uses characters '/', '+' and '-' and we want only alphanumeric characters
		return base32.encodeToString(byteBuffer.array());		
	}
	
	@Override
	@Transactional
	public void createForeignPaymentOrder(ForeignPayment payment) {
		ForeignPaymentOrder paymentOrder = createOrderFromPayment(payment, ForeignPaymentOrder.PaymentStatus.NEW, null);
		getEntityManager().persist(paymentOrder);
	}

	@Override
	@Transactional
	public void createPreOrderWaitingForBTC(ForeignPayment preOrder, String acceptingBtcAddress) {
		ForeignPaymentOrder paymentOrder = createOrderFromPayment(preOrder, ForeignPaymentOrder.PaymentStatus.WAITING_FOR_BTC, acceptingBtcAddress);
		getEntityManager().persist(paymentOrder);
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
		preOrder.setUpdateTimestamp(timeProvider.nowCalendar());
		preOrder.setStatus(PaymentStatus.NEW);
		
		getEntityManager().persist(preOrder);
	}

	private EntityManager getEntityManager() {
		return entityManagerProvider.get();
	}
	


}
