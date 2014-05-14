package com.newpecunia.bitstamp.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.google.inject.Inject;
import com.newpecunia.bitstamp.service.BitstampService;
import com.newpecunia.bitstamp.service.OrderBook;

@DisallowConcurrentExecution
public class OrderBookDownloaderJob implements Job {

	private static final Logger logger = LogManager.getLogger(OrderBookDownloaderJob.class);	
	
	private BitstampService bitstampService;
	private CachedOrderBookHolderImpl orderBookHolder;

	@Inject
	OrderBookDownloaderJob(BitstampService bitstampService, CachedOrderBookHolderImpl orderBookHolder) {
		this.bitstampService = bitstampService;
		this.orderBookHolder = orderBookHolder;
	}
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			OrderBook orderBook = bitstampService.getOrderBook();
			orderBookHolder.setOrderBook(orderBook);
		} catch (Exception e) {
			if (orderBookHolder.isOrderBookActual()) {
				logger.info("Error ocurred while downloading Order Book from Bitstamp.");
			} else {
				logger.error("Could not download orderBook! Bitstamp down?");
			}
		}
	}

}
