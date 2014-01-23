package com.newpecunia.trader.service.impl;

import java.math.BigDecimal;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import com.newpecunia.persistence.entities.BuyPreorder;
import com.newpecunia.persistence.entities.BuyPreorder.BuyPreorderResolution;
import com.newpecunia.trader.service.BuyService;

public class BuyServiceImpl implements BuyService {

	private static final Logger logger = LogManager.getLogger(BuyServiceImpl.class);
	private Provider<EntityManager> entityManagerProvider;	
	
	@Inject
	BuyServiceImpl(Provider<EntityManager> entityManagerProvider) {
		this.entityManagerProvider = entityManagerProvider;
	}
	
	@Override
	@Transactional
	public void createPreOrder(BigDecimal btcAmount, BigDecimal usdAmount, String btcAddress, String email, String cardTransactionId) {
		logger.info(String.format("Creating buy order - %s BTC for %s USD to address %s, email: %s. Waiting for card payment transaction %s.", btcAmount.toPlainString(), usdAmount.toPlainString(), btcAddress, email, cardTransactionId));

		BuyPreorder preorder = new BuyPreorder();
		preorder.setBtcAmount(btcAmount);
		preorder.setUsdAmount(usdAmount);
		preorder.setBtcAddress(btcAddress);
		preorder.setEmail(email);
		preorder.setCardTransactionId(cardTransactionId);
		preorder.setStatus(BuyPreorderResolution.OPEN);
		
		getEntityManager().persist(preorder);
	}
	
	private EntityManager getEntityManager() {
		return entityManagerProvider.get();
	}	

}
