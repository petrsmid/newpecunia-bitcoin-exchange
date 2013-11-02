package com.newpecunia.scheduler;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzContextListener implements ServletContextListener{

	private static final Logger logger = LogManager.getLogger(QuartzContextListener.class);	
	
	private Scheduler scheduler = null;
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.start();
		} catch (SchedulerException e) {
			String msg = "Could not start Quartz scheduler";
			logger.error(msg, e);
			throw new RuntimeException(msg, e);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		if (scheduler != null) {
			try {
				scheduler.shutdown(true); //wait for jobs to complete
			} catch (SchedulerException e) {
				String msg = "Error ocurred while stopping scheduler";
				logger.error(msg, e);
				throw new RuntimeException(msg, e);
			}
		} else {
			logger.info("Scheduler was not started.");
		}
	}

	
}
