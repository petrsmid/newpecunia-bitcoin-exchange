package com.newpecunia.trader.service.impl.processor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.google.inject.Inject;

@DisallowConcurrentExecution
public class BitstampAutoTraderJob implements Job {

	private static final Logger logger = LogManager.getLogger(BitstampAutoTraderJob.class);	
	
	
	private BitstampAutoTrader autoTrader;

	@Inject
	BitstampAutoTraderJob(BitstampAutoTrader autoTrader) {
		this.autoTrader = autoTrader;
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			autoTrader.trade();
		} catch (Exception e) {
			logger.error("Error ocurred while trading", e);
		}
	}
	
}
