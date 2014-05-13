package com.newpecunia.trader.service.impl;

import java.math.BigDecimal;

import javax.persistence.EntityManager;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import com.newpecunia.countries.Country;
import com.newpecunia.persistence.entities.BuyPreorder;
import com.newpecunia.persistence.entities.BuyPreorder.BuyPreorderResolution;
import com.newpecunia.persistence.entities.UnconfirmedBuyPreOrder;
import com.newpecunia.time.TimeProvider;
import com.newpecunia.trader.service.BuyService;

public class BuyServiceImpl implements BuyService {

	private static final Logger logger = LogManager.getLogger(BuyServiceImpl.class);
	private Provider<EntityManager> entityManagerProvider;
	private TimeProvider timeProvider;
	private MapperFactory mapperFactory;	
	
	@Inject
	BuyServiceImpl(Provider<EntityManager> entityManagerProvider, TimeProvider timeProvider, MapperFactory mapperFactory) {
		this.entityManagerProvider = entityManagerProvider;
		this.timeProvider = timeProvider;
		this.mapperFactory = mapperFactory;
	}
	
	@Override
	@Transactional
	public void createPreOrder(BigDecimal btcAmount, BigDecimal usdAmount,
			String btcAddress, String email, String cardTransactionId,
			String name, String city, String countryCode, String txId) {
		logger.info(String.format("Creating buy order - %s BTC for %s USD to address %s, email: %s. Waiting for card payment transaction %s.", btcAmount.toPlainString(), usdAmount.toPlainString(), btcAddress, email, cardTransactionId));

		BuyPreorder preorder = new BuyPreorder();
		preorder.setBtcAmount(btcAmount);
		preorder.setUsdAmount(usdAmount);
		preorder.setBtcAddress(btcAddress);
		preorder.setEmail(email);
		preorder.setName(email);
		preorder.setCity(email);
		preorder.setCountryCode(email);
		preorder.setCardTransactionId(cardTransactionId);
		preorder.setStatus(BuyPreorderResolution.OPEN);
		
		getEntityManager().persist(preorder);
	}
	
	private EntityManager getEntityManager() {
		return entityManagerProvider.get();
	}

	@Override
	@Transactional
	public String createUnconfirmedPreorder(BigDecimal btcAmount,
			String btcAddress, String email, String name, String address,
			String city, Country country) {
		logger.info(String.format("Creating unconfirmed buy order - %s BTC to address %s, email: %s", btcAmount.toPlainString(), btcAddress, email));

		UnconfirmedBuyPreOrder unconfirmedPreorder = new UnconfirmedBuyPreOrder();
		unconfirmedPreorder.setBtcAmount(btcAmount);
		unconfirmedPreorder.setBtcAddress(btcAddress);
		unconfirmedPreorder.setEmail(email);
		unconfirmedPreorder.setName(name);
		unconfirmedPreorder.setAddress(address);
		unconfirmedPreorder.setCity(city);
		unconfirmedPreorder.setCountryCode(country.getIsoCode());
		
		unconfirmedPreorder.setCreateTimestamp(timeProvider.nowCalendar());

		getEntityManager().persist(unconfirmedPreorder);
		
		return unconfirmedPreorder.getId();
	}

	@Override
	public com.newpecunia.trader.service.UnconfirmedBuyPreOrder getUnconfirmedPreorder(String id) {
		logger.info(String.format("Looking for unconfirmed buy order %s", id));
		
		UnconfirmedBuyPreOrder unconfirmedPreorder = getEntityManager().find(UnconfirmedBuyPreOrder.class, id);
		
		MapperFacade mapper = mapperFactory.getMapperFacade(); //TODO create test which validates that the mapping works
		return mapper.map(unconfirmedPreorder, com.newpecunia.trader.service.UnconfirmedBuyPreOrder.class);
	}

}
