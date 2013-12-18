package com.newpecunia.trader.service.impl.processor;

import java.math.BigDecimal;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import com.newpecunia.persistence.entities.BitstampWithdrawalRests;

@Singleton
public class BitstampWithdrawOrderManagerImpl implements BitstampWithdrawOrderManager {

	private static final Logger logger = LogManager.getLogger(BitstampWithdrawOrderManagerImpl.class);	

	private Provider<EntityManager> emProvider;

	@Inject
	BitstampWithdrawOrderManagerImpl(Provider<EntityManager> emProvider) {
		this.emProvider = emProvider;
	}
	
	@Override
	@Transactional
	public void orderWithdrawUsdToUnicredit(BigDecimal amount) {
		addUsd(amount);
	}

	@Override
	@Transactional
	public void orderWithdrawBtcFromBitstampToWallet(BigDecimal amount) {
		addBtc(amount);
	}
	
	@Override
	@Transactional
	public void withdrawOfUsdFullfilled(BigDecimal amount) {
		addUsd(amount.multiply(new BigDecimal(-1)));
	}

	@Override
	@Transactional
	public void withdrawOfBtcFullfilled(BigDecimal amount) {
		addBtc(amount.multiply(new BigDecimal(-1)));
	}
	
	@Override
	@Transactional
	public BigDecimal getUsdAmountToWithdraw() {
		logger.trace("Getting USD amount to withdraw.");
		EntityManager entityManager = emProvider.get();
		BitstampWithdrawalRests rests = entityManager.find(BitstampWithdrawalRests.class, BitstampWithdrawalRests.CONSTANT_ID, LockModeType.PESSIMISTIC_WRITE);
		return rests.getUsdsToWithdraw();
	}
	
	@Override
	@Transactional
	public BigDecimal getBtcAmountToWithdraw() {
		logger.trace("Getting BTC amount to withdraw.");
		EntityManager entityManager = emProvider.get();
		BitstampWithdrawalRests rests = entityManager.find(BitstampWithdrawalRests.class, BitstampWithdrawalRests.CONSTANT_ID, LockModeType.PESSIMISTIC_WRITE);
		return rests.getBtcsToWithdraw();
	}

	
	private void addBtc(BigDecimal amount) {
		logger.trace("Adding BTC amount to withdraw.");
		EntityManager entityManager = emProvider.get();
		BitstampWithdrawalRests rests = entityManager.find(BitstampWithdrawalRests.class, BitstampWithdrawalRests.CONSTANT_ID, LockModeType.PESSIMISTIC_WRITE);
		BigDecimal actualBtcsToWithdraw = rests.getBtcsToWithdraw();
		BigDecimal newBtcsToWithdraw = actualBtcsToWithdraw.add(amount);
		rests.setBtcsToWithdraw(newBtcsToWithdraw);
		entityManager.persist(rests);
	}

	private void addUsd(BigDecimal amount) {
		logger.trace("Adding USD amount to withdraw.");
		EntityManager entityManager = emProvider.get();
		BitstampWithdrawalRests rests = entityManager.find(BitstampWithdrawalRests.class, BitstampWithdrawalRests.CONSTANT_ID, LockModeType.PESSIMISTIC_WRITE);
		BigDecimal actualUsdsToWithdraw = rests.getUsdsToWithdraw();
		BigDecimal newUsdsToWithdraw = actualUsdsToWithdraw.add(amount);
		rests.setUsdsToWithdraw(newUsdsToWithdraw);
		entityManager.persist(rests);
	}

	
}
