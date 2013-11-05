package com.newpecunia.unicredit.service.impl.processor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class PaymentProcessorJobsSetuper {
	
	private static final Logger logger = LogManager.getLogger(PaymentProcessorJobsSetuper.class);	
	
	private Scheduler scheduler;

	@Inject
	PaymentProcessorJobsSetuper(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
	
	public void setupJobs() {
		logger.info("Setting up jobs");
		preparePreorderCleanerJob();
		preparePaymentPackageUploaderJob();
		preparePaymentPackageStatusUpdaterJob();
		preparePaymentStatementStatusUpdaterJob();
		preparePaymentStatusReportingJob();
	}

	
	// Quartz syntax:  Seconds  Minutes  Hours  Day-of-Month  Month  Day-of-Week  Year(optional) 
	
	
	private void preparePreorderCleanerJob() {
		setupJobAndTrigger(PreorderCleanerJob.class, 
				CronScheduleBuilder.cronSchedule("0 2/3 * * * ?")); //every 3 minutes starting after 2 minutes
	}

	private void preparePaymentPackageUploaderJob() {
		setupJobAndTrigger(PaymentPackageUploaderJob.class, 
				CronScheduleBuilder.cronSchedule("0 0/5 * * * ?")); //every 5 minutes starting immediately
	}

	private void preparePaymentPackageStatusUpdaterJob() {
		setupJobAndTrigger(PaymentPackageStatusUpdaterJob.class, 
				CronScheduleBuilder.cronSchedule("0 0/11 * * * ?")); //every 11 minutes
	}

	private void preparePaymentStatementStatusUpdaterJob() {
		setupJobAndTrigger(PaymentStatementStatusUpdaterJob.class, 
				CronScheduleBuilder.cronSchedule("0 4/14 * * * ?")); //every 14 minutes starting after 4 minutes
	}

	private void preparePaymentStatusReportingJob() {
		setupJobAndTrigger(PaymentStatusReportingJob.class, 
				CronScheduleBuilder.cronSchedule("0 0 16 * * ?")); //every day at 16:00
	}


	

	private void setupJobAndTrigger(Class<? extends Job> jobClass, ScheduleBuilder<?> schedule) {
		String jobName = jobClass.getSimpleName();
		String jobTrigger = jobName+"Trigger";
		
		JobDetail jobDetail = JobBuilder.newJob(jobClass)
				.withIdentity(jobName, jobName).build();

		Trigger trigger = TriggerBuilder.newTrigger().withIdentity(jobTrigger)
				.withSchedule(schedule).build();
		
		try {
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			logger.error("Could not configure Quartz job "+jobName);
		}  
	}
	
}
