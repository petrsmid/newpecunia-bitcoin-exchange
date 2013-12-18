package com.newpecunia.scheduler;

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
import com.newpecunia.trader.service.impl.processor.BitstampAutoTraderJob;
import com.newpecunia.trader.service.impl.processor.BtcOrderProcessorAndSettleUpJob;
import com.newpecunia.trader.service.impl.processor.MoneySettleUpJob;
import com.newpecunia.unicredit.service.impl.processor.PaymentPackageStatusUpdaterJob;
import com.newpecunia.unicredit.service.impl.processor.PaymentPackageUploaderJob;
import com.newpecunia.unicredit.service.impl.processor.PaymentStatementStatusUpdaterJob;
import com.newpecunia.unicredit.service.impl.processor.PaymentStatusReportingJob;
import com.newpecunia.unicredit.service.impl.processor.PreorderCleanerJob;

@Singleton
public class JobsSetuper {
	
	private static final Logger logger = LogManager.getLogger(JobsSetuper.class);	
	
	private Scheduler scheduler;

	@Inject
	JobsSetuper(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
	
	public void setupJobs() {
		logger.info("Setting up jobs");
		preparePreorderCleanerJob();
		preparePaymentPackageUploaderJob();
		preparePaymentPackageStatusUpdaterJob();
		preparePaymentStatementStatusUpdaterJob();
		preparePaymentStatusReportingJob();
		
		prepareBitstampAutoTraderJob();
		prepareMoneySettleUpJob();
		prepareBtcOrderProcessorAndSettleUpJob();
	}

	
	// Quartz syntax:  Seconds  Minutes  Hours  Day-of-Month  Month  Day-of-Week  Year(optional) 
	
	
	private void prepareBitstampAutoTraderJob() {
		setupJobAndTrigger(BitstampAutoTraderJob.class, 
				CronScheduleBuilder.cronSchedule("0/21 * * * * ?")); //every 21 seconds
							//Note: when using BitstampAutoTraderPrefferingUSD (default auto trader implementation)
							//the implementation performs withdraw of USD only every day at 8:00.
							//If you want to change it please change the constant 
							//BitstampAutoTraderPrefferingUSD.WITHDRAW_USD_HOUR
	}

	private void prepareBtcOrderProcessorAndSettleUpJob() {
		setupJobAndTrigger(BtcOrderProcessorAndSettleUpJob.class, 
				CronScheduleBuilder.cronSchedule("11/21 * * * * ?")); //every 21 seconds starting after 11 seconds
	}

	private void prepareMoneySettleUpJob() {
		setupJobAndTrigger(MoneySettleUpJob.class, 
				CronScheduleBuilder.cronSchedule("0 0 9 * * ?")); //every day at 9:00
	}

	private void preparePreorderCleanerJob() {
		setupJobAndTrigger(PreorderCleanerJob.class, 
				CronScheduleBuilder.cronSchedule("0 2/3 * * * ?")); //every 3 minutes starting after 2 minutes
	}

	private void preparePaymentPackageUploaderJob() {
		setupJobAndTrigger(PaymentPackageUploaderJob.class, 
				CronScheduleBuilder.cronSchedule("0 1/5 * * * ?")); //every 5 minutes starting after 1 minute
	}

	private void preparePaymentPackageStatusUpdaterJob() {
		setupJobAndTrigger(PaymentPackageStatusUpdaterJob.class, 
				CronScheduleBuilder.cronSchedule("0 3/11 * * * ?")); //every 11 minutes starting after 3 minutes
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
