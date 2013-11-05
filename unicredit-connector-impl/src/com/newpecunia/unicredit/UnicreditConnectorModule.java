package com.newpecunia.unicredit;

import com.google.inject.AbstractModule;
import com.newpecunia.unicredit.service.BalanceService;
import com.newpecunia.unicredit.service.PaymentService;
import com.newpecunia.unicredit.service.impl.BalanceServiceImpl;
import com.newpecunia.unicredit.service.impl.PaymentServiceImpl;
import com.newpecunia.unicredit.service.impl.processor.PaymentProcessorJobsSetuper;
import com.newpecunia.unicredit.service.impl.processor.PaymentProcessorJobsSetuper;
import com.newpecunia.unicredit.webdav.UnicreditWebdavService;
import com.newpecunia.unicredit.webdav.impl.GpgFileSigner;
import com.newpecunia.unicredit.webdav.impl.GpgFileSignerImpl;
import com.newpecunia.unicredit.webdav.impl.UnicreditWebdavServiceImpl;

public class UnicreditConnectorModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(BalanceService.class).to(BalanceServiceImpl.class);
		bind(PaymentService.class).to(PaymentServiceImpl.class);
		bind(UnicreditWebdavService.class).to(UnicreditWebdavServiceImpl.class);
		bind(GpgFileSigner.class).to(GpgFileSignerImpl.class);
	}


}
