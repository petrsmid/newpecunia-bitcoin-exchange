package com.newpecunia.scheduler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.JobFactory;

import com.google.inject.AbstractModule;

public class SchedulerModule extends AbstractModule {

	private static final Logger logger = LogManager.getLogger(SchedulerModule.class);	
	
	@Override
	protected void configure() {
		bind(Scheduler.class).toInstance(constructQuartzScheduler());
		bind(JobFactory.class).to(GuiceAwareJobFactory.class); //Quartz job factory
	}

	
	private Scheduler constructQuartzScheduler() {
		try {
			return StdSchedulerFactory.getDefaultScheduler();
		} catch (SchedulerException e) {
			String msg = "Could not start Quartz scheduler";
			logger.error(msg, e);
			throw new RuntimeException(msg, e);
		}
	}
	
}
