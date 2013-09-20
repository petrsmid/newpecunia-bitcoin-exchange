package com.newpecunia.unicredit.service.impl.processor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.google.inject.Inject;
import com.newpecunia.time.TimeProvider;
import com.newpecunia.unicredit.service.impl.entity.ForeignPaymentOrder;
import com.newpecunia.unicredit.service.impl.entity.ForeignPaymentOrder.PaymentStatus;
import com.newpecunia.unicredit.webdav.Status.PackageStatus;
import com.newpecunia.unicredit.webdav.UnicreditWebdavService;

public class PaymentStatusUpdaterJob implements Runnable {

	private static final Logger logger = LogManager.getLogger(PaymentStatusUpdaterJob.class);	
	
	private SessionFactory sessionFactory;
	private UnicreditWebdavService webdavService;
	private TimeProvider timeProvider;

	@Inject
	public PaymentStatusUpdaterJob(SessionFactory sessionFactory, UnicreditWebdavService webdavService, TimeProvider timeProvider) {
		this.sessionFactory = sessionFactory;
		this.webdavService = webdavService;
		this.timeProvider = timeProvider;
	}
	
	@Override
	public void run() {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		updateStatusOfUnsignedPayments(session);
		
		session.flush();
		tx.commit();
		session.close();		
	}

	private void updateStatusOfUnsignedPayments(Session session) {
		List<ForeignPaymentOrder> unsignedPayments = loadUnsignedPayments(session);
		
		List<String> statusIds = null;
		List<String> uploadedIds = null;
		try {
			uploadedIds = webdavService.listForeignUploadedPackages();
			statusIds = webdavService.listPackagesWithStatus();
		} catch (IOException e) {
			logger.warn("Webdav out of order. Could not obtain package statuses.",e);
			return;
		}
		
		List<String> newlySignedPackages = new ArrayList<>();
		List<String> stillUnsignedPackages = new ArrayList<>();
		
		for (ForeignPaymentOrder unsignedPayment : unsignedPayments) {
			String paymentId = unsignedPayment.getId();
			if (statusIds.contains(paymentId)) {
				try {
					PackageStatus pckgStatus = webdavService.getStatusOfPackage(paymentId).getPackageStatus();
					if (PackageStatus.SIGNED.equals(pckgStatus)) {
						newlySignedPackages.add(paymentId);
					} else {
						stillUnsignedPackages.add(paymentId);
					}
					updatePaymentStatusInDB(session, unsignedPayment, getPaymentStatusByPackageStatus(pckgStatus));					
				} catch (IOException e) {
					logger.error("Could not read status of package "+paymentId, e);
					continue;
				}
			} else {
				//status not found for a payment
				if (uploadedIds.contains(paymentId)) {
					logger.warn("Status file of payment "+paymentId+" not found.");
					updatePaymentStatusInDB(session, unsignedPayment, PaymentStatus.WEBDAV_PENDING);
				} else {
					updatePaymentStatusInDB(session, unsignedPayment, PaymentStatus.ERROR);
					logger.error("No uploaded file nor status file for payment "+paymentId+" was found.");
				}
			}
		}
		
		logger.info("Check of packages status finished. Newly signed packages: "+ newlySignedPackages+" Still unsigned packages: "+stillUnsignedPackages);
	}

	private PaymentStatus getPaymentStatusByPackageStatus(PackageStatus packageStatus) {
		PaymentStatus paymentStatus = null;
		switch (packageStatus) {
		case PREPARING:
		case PARTLY_SIGNED:
			paymentStatus = PaymentStatus.WEBDAV_PENDING;
			break;
		case SIGNED:
			paymentStatus = PaymentStatus.WEBDAV_SIGNED;
			break;
		case ERROR:
			paymentStatus = PaymentStatus.WEBDAV_ERROR;
			break;
		}
		return paymentStatus;
	}

	private void updatePaymentStatusInDB(Session session, ForeignPaymentOrder payment, PaymentStatus newStatus) {
		PaymentStatus oldStatus = payment.getStatus();
		if (!oldStatus.equals(newStatus)) {
			payment.setStatus(newStatus);		
			payment.setUpdateTimestamp(timeProvider.nowCalendar());
			session.update(payment);
		}
	}

	@SuppressWarnings("unchecked")
	private List<ForeignPaymentOrder> loadUnsignedPayments(Session session) {
		return session.createCriteria(ForeignPaymentOrder.class)
			.add(Restrictions.eq("status", PaymentStatus.SENT_TO_WEBDAV))
			.addOrder(Order.asc("updateTimestamp"))
			.list();
	}

}
