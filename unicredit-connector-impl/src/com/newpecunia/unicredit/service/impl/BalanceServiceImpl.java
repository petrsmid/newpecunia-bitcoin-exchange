package com.newpecunia.unicredit.service.impl;

import java.io.IOException;
import java.math.BigDecimal;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import com.newpecunia.configuration.NPConfiguration;
import com.newpecunia.persistence.entities.ForeignPaymentOrder;
import com.newpecunia.persistence.entities.ForeignPaymentOrder.PaymentStatus;
import com.newpecunia.persistence.entities.LastKnownBalance;
import com.newpecunia.unicredit.service.BalanceService;
import com.newpecunia.unicredit.webdav.UnicreditWebdavService;

@Singleton
public class BalanceServiceImpl implements BalanceService {

	private static final Logger logger = LogManager.getLogger(BalanceServiceImpl.class);	
	
	private UnicreditWebdavService webdavService;
	private Provider<EntityManager> emProvider;
	private BigDecimal paymentFee;

	@Inject
	BalanceServiceImpl(Provider<EntityManager> emProvider, UnicreditWebdavService webdavService, NPConfiguration configuration) {
		this.emProvider = emProvider;
		this.webdavService = webdavService;
		this.paymentFee = configuration.getPaymentFee();
	}
	
	private Criterion getFilterCriterion() {
		return Restrictions.in("status", new PaymentStatus[] {
				PaymentStatus.NEW,
				PaymentStatus.SENT_TO_WEBDAV,
				PaymentStatus.WEBDAV_PENDING,
				PaymentStatus.WEBDAV_SIGNED,
		});
	}
	
	@Override
	@Transactional
	public BigDecimal getApproximateBalance() {
		logger.trace("Getting approximate balance.");
		BigDecimal lastKnownBalance = null;
		try {
			lastKnownBalance = webdavService.getLastBalance();
			if (lastKnownBalance == null) { //no balance known yet or no transactions for last 30 days
				lastKnownBalance = readLastKnownId();
			}
		} catch (IOException e) { //problem while connecting to webdav
			logger.error("Cannot connect to webdav.", e);
			lastKnownBalance = readLastKnownId();
		}
		
		Session session = emProvider.get().unwrap(Session.class);
		BigDecimal unbilledOrdersSumAmount = (BigDecimal) session.createCriteria(ForeignPaymentOrder.class)
				.add(getFilterCriterion())
				.setProjection(Projections.sum("amount"))
				.uniqueResult();
		if (unbilledOrdersSumAmount == null) {
			unbilledOrdersSumAmount = BigDecimal.ZERO;
		}

		long unbilledOrdersCount = (long) session.createCriteria(ForeignPaymentOrder.class)
				.add(getFilterCriterion())
				.setProjection(Projections.rowCount())
				.uniqueResult();
		
		BigDecimal fees = paymentFee.multiply(new BigDecimal(unbilledOrdersCount));
		
		BigDecimal actualBalance = lastKnownBalance.subtract(unbilledOrdersSumAmount).subtract(fees);
		saveActualBalance(actualBalance);
		return actualBalance;
		
	}

	private void saveActualBalance(BigDecimal actualBalance) {
		EntityManager entityManager = emProvider.get();
		LastKnownBalance balance = entityManager.find(LastKnownBalance.class, LastKnownBalance.CONSTANT_ID);
		balance.setBalance(actualBalance);
		entityManager.persist(balance);
	}

	private BigDecimal readLastKnownId() {
		EntityManager entityManager = emProvider.get();
		LastKnownBalance balance = entityManager.find(LastKnownBalance.class, LastKnownBalance.CONSTANT_ID);
		return balance.getBalance();
	}

}
