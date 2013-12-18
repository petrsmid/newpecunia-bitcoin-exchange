package com.newpecunia.trader.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import com.newpecunia.persistence.entities.BtcPaymentOrder;
import com.newpecunia.persistence.entities.BtcPaymentOrder.BtcOrderStatus;
import com.newpecunia.time.TimeProvider;
import com.newpecunia.trader.service.TraderService;
import com.newpecunia.unicredit.service.PaymentService;

@Singleton
public class TraderServiceImpl implements TraderService {

	private static final Logger logger = LogManager.getLogger(TraderServiceImpl.class);	
	
	private PaymentService paymentService;
	private CachedBuySellPriceCalculator cachedBuySellPriceCalc;
	private TimeProvider timeProvider;
	private Provider<EntityManager> emProvider;

	
	@Inject
	TraderServiceImpl(
			PaymentService paymentService, 
			CachedBuySellPriceCalculator cachedBuySellPriceCalc,
			TimeProvider timeProvider,
			Provider<EntityManager> emProvider) {
		
		this.paymentService = paymentService;
		this.cachedBuySellPriceCalc = cachedBuySellPriceCalc;
		this.timeProvider = timeProvider;
		this.emProvider = emProvider;
	}
	
	@Override
	public BigDecimal getNPBtcBuyPriceInUSD(BigDecimal amountBtc) {
		return cachedBuySellPriceCalc.getBtcBuyPriceInUSD();
	}

	@Override
	public BigDecimal getNPBtcSellPriceInUSD(BigDecimal amountBtc) {
		return cachedBuySellPriceCalc.getBtcSellPriceInUSD();
	}

	@Override
	public void payForReceivedBTCs(String receivingBtcAddress, BigDecimal btcAmount) {
		logger.trace(String.format("Paying for received BTCs - receiving address: %s, amount %s BTC", receivingBtcAddress, btcAmount));
		BigDecimal amountUSD = getNPBtcBuyPriceInUSD(btcAmount);
		paymentService.createOrderFromPreOrder(receivingBtcAddress, amountUSD);
	}
	
	@Override
	@Transactional
	public void sendBTCsForPayment(String receiverBtcAddress, BigDecimal amount, String email) {
		logger.info("Creating BTC order - "+amount.toPlainString()+" BTC  - "+receiverBtcAddress+"  - "+ email);
		BtcPaymentOrder btcOrder = new BtcPaymentOrder();
		btcOrder.setAddress(receiverBtcAddress);
		btcOrder.setAmount(amount);
		btcOrder.setEmail(email);
		btcOrder.setStatus(BtcOrderStatus.UNPROCESSED);
		Calendar now = timeProvider.nowCalendar();
		btcOrder.setCreateTimestamp(now);
		btcOrder.setUpdateTimestamp(now);
		emProvider.get().persist(btcOrder);
	}

}
