package com.newpecunia;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.newpecunia.bitstamp.BitstampConnectorModule;
import com.newpecunia.bitstamp.service.BitstampService;
import com.newpecunia.bitstamp.service.BitstampServiceException;
import com.newpecunia.bitstamp.service.OrderBook;
import com.newpecunia.bitstamp.service.PriceAndAmount;
import com.newpecunia.common.CommonModule;

public class LowestPriceForVolume {

	private BigDecimal VOLUME = new BigDecimal(113);
	
	private static final long CHECK_PERIOD_IN_MS = 11*1000; //11 seconds
	
	private BitstampService bitstampService;
	
	LowestPriceForVolume(BitstampService bitstampService) throws IOException {
		this.bitstampService = bitstampService;
	}
	
	private void performCheck() throws BitstampServiceException, FileNotFoundException, IOException {
		OrderBook orderBook = bitstampService.getOrderBook();
		List<PriceAndAmount> bids = orderBook.getBids();
		BigDecimal sum = BigDecimal.ZERO;
		BigDecimal lastPrice = null;
		for (PriceAndAmount bid : bids) {
			BigDecimal amount = bid.getAmount();
			sum = sum.add(amount);
			if (sum.compareTo(VOLUME) > 0) {
				log(""+(new Date()).toString()+"  Minimum possible price for volume "+VOLUME.toPlainString()+": "+lastPrice.toPlainString(), null);
				break;
			} else {
				lastPrice = bid.getPrice();
			}
		}
	}
	
	private void checkPeriodically() {
		while (true) {
			try {
				performCheck();
			} catch (BitstampServiceException | IOException e) {
				log("Error ocurred while checking Bitstamp.", e);
			} finally {
				try {
					Thread.sleep(CHECK_PERIOD_IN_MS);
				} catch (InterruptedException e) {
					log("Error while sleeping.", e);
				}
			}
		}
	}
	
	
	public static void main(String[] args) throws IOException {
		Injector injector = Guice.createInjector(
				new CommonModule(),
        		new BitstampConnectorModule());
		
		LowestPriceForVolume checker = new LowestPriceForVolume(injector.getInstance(BitstampService.class));
		
		checker.checkPeriodically();
	}
	
	private void log(String msg, Throwable cause) {
		System.out.println(msg);
		if (cause != null) {
			cause.printStackTrace();
		}
	}
	
	
	
}
