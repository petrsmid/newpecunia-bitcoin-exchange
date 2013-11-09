package com.newpecunia.scheduler;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

import com.google.inject.Injector;
import com.newpecunia.ioc.InjectorHolder;

public class QuartzStartStopContextListener implements ServletContextListener{

	private static final Logger logger = LogManager.getLogger(QuartzStartStopContextListener.class);	
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			Injector injector = InjectorHolder.getinjector();
			Scheduler scheduler = injector.getInstance(Scheduler.class);
			scheduler.setJobFactory(injector.getInstance(GuiceAwareJobFactory.class)); 
			
			//setup jobs
			JobsSetuper jobsSetuper = injector.getInstance(JobsSetuper.class);
			jobsSetuper.setupJobs();
			
			scheduler.start();
		} catch (SchedulerException e) {
			logger.error("Error occurred while starting Quartz Scheduler.");
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		Injector injector = InjectorHolder.getinjector();
		Scheduler scheduler = injector.getInstance(Scheduler.class);
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
