package com.newpecunia.trader.service.impl;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.newpecunia.bitcoind.BitcoindConnectorModule;
import com.newpecunia.bitstamp.BitstampConnectorModule;
import com.newpecunia.common.CommonModule;
import com.newpecunia.persistence.PersistenceModule;
import com.newpecunia.trader.TraderModule;
import com.newpecunia.unicredit.UnicreditConnectorModule;

public class CachedBuySellPriceCalculatorLiveTest {

	private CachedBuySellPriceCalculator calculator;


	@Before
	public void setup() {
		Injector injector = Guice.createInjector(
				new TraderModule(), 
				new CommonModule(), 
				new BitstampConnectorModule(), 
				new BitcoindConnectorModule(),
				new UnicreditConnectorModule(),
				new PersistenceModule(),
        		new JpaPersistModule("testingJpaUnit")        		
				);
		
		calculator = injector.getInstance(CachedBuySellPriceCalculator.class);
	}
	
	
	@Test
	public void test() {
		BigDecimal buyPrice = calculator.getBtcBuyPriceInUSD();
		BigDecimal sellPrice = calculator.getBtcSellPriceInUSD();
		Assert.assertNotNull(buyPrice);
		Assert.assertNotNull(sellPrice);
		Assert.assertTrue(buyPrice.compareTo(sellPrice) > 0);
		
		
	}
}
