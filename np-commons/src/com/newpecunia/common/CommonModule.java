package com.newpecunia.common;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.JobFactory;

import com.google.inject.AbstractModule;
import com.newpecunia.countries.CountryDatabase;
import com.newpecunia.countries.JavaCountryDatabase;
import com.newpecunia.email.EmailSender;
import com.newpecunia.email.EmailSenderImpl;
import com.newpecunia.scheduler.GuiceAwareJobFactory;
import com.newpecunia.synchronization.LockProvider;
import com.newpecunia.synchronization.SingleJVMLockProvider;
import com.newpecunia.time.TimeProvider;
import com.newpecunia.time.TimeProviderImpl;

public class CommonModule extends AbstractModule {

	private static final Logger logger = LogManager.getLogger(CommonModule.class);	
	
	
	@Override
	protected void configure() {
		bind(LockProvider.class).to(SingleJVMLockProvider.class);
		bind(CountryDatabase.class).to(JavaCountryDatabase.class);
		bind(TimeProvider.class).to(TimeProviderImpl.class);
		bind(MapperFactory.class).toInstance(new DefaultMapperFactory.Builder().build());
		bind(EmailSender.class).to(EmailSenderImpl.class);
		//Quartz
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
