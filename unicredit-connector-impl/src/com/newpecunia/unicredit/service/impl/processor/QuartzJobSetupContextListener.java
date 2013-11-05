package com.newpecunia.unicredit.service.impl.processor;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.google.inject.Injector;
import com.newpecunia.ioc.InjectorHolder;

public class QuartzJobSetupContextListener implements ServletContextListener{

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		Injector injector = InjectorHolder.getinjector();
		PaymentProcessorJobsSetuper paymentProcessor = injector.getInstance(PaymentProcessorJobsSetuper.class);
		paymentProcessor.setupJobs();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		//do nothing
	}

	
}
