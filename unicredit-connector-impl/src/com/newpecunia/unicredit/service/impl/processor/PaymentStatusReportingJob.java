package com.newpecunia.unicredit.service.impl.processor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.newpecunia.configuration.NPConfiguration;
import com.newpecunia.email.EmailSender;
import com.newpecunia.persistence.entities.ForeignPaymentOrder;
import com.newpecunia.persistence.entities.ForeignPaymentOrder.PaymentStatus;

public class PaymentStatusReportingJob implements Runnable {

	
	private Provider<EntityManager> emProvider;
	private EmailSender emailSender;
	private NPConfiguration configuration;

	private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	@Inject
	PaymentStatusReportingJob(Provider<EntityManager> emProvider, EmailSender emailSender, NPConfiguration configuration) {
		this.emProvider = emProvider;
		this.emailSender = emailSender;
		this.configuration = configuration;
	}
	
	@Override
	public void run() {
		List<ForeignPaymentOrder> pendingOrErrorOrders = loadPendingOrErrorPayments(emProvider.get().unwrap(Session.class));
		
		List<ForeignPaymentOrder> errorOrders = filterByStatus(pendingOrErrorOrders, 
				PaymentStatus.ERROR, PaymentStatus.WEBDAV_ERROR, PaymentStatus.REJECTED);
		
		List<ForeignPaymentOrder> unprocessedByWebdavOrders = filterByStatus(pendingOrErrorOrders, 
				PaymentStatus.NEW, PaymentStatus.SENT_TO_WEBDAV, PaymentStatus.WEBDAV_PENDING);
		
		List<ForeignPaymentOrder> waitingForBankOrders = filterByStatus(pendingOrErrorOrders, 
				PaymentStatus.WEBDAV_SIGNED);
		 
		
		StringBuilder sb = new StringBuilder();

		if (!errorOrders.isEmpty()) {
			sb.append("\n");
			sb.append("Orders in status ERROR:\n");
			reportOrders(errorOrders, sb);
		}

		
		if (!unprocessedByWebdavOrders.isEmpty()) {
			sb.append("\n");
			sb.append("Orders NOT processed by Webdav:\n");
			reportOrders(unprocessedByWebdavOrders, sb);
		}
		
		if (!waitingForBankOrders.isEmpty()) {
			sb.append("\n");
			sb.append("Orders waiting in bank:\n");
			reportOrders(waitingForBankOrders, sb);
		}		
		
		emailSender.sendEmail(configuration.getReportingEmailAddress(), "Daily report of payments states", sb.toString());
	}

	private void reportOrders(List<ForeignPaymentOrder> orders, StringBuilder sb) {
		for (ForeignPaymentOrder order : orders) {
			sb.append(order.getId());
			sb.append("  - created: ");
			sb.append(formatTime(order.getCreateTimestamp()));
			sb.append(", last updated: ");
			sb.append(formatTime(order.getUpdateTimestamp()));
			sb.append(", status: ");
			sb.append(order.getStatus());
			sb.append(", requestor e-mail: ");
			sb.append(order.getRequestorEmail());
			sb.append("\n");
		}
	}
	
	private String formatTime(Calendar cal) {
		if (cal == null) {return "";}
		return dateFormatter.format(cal.getTime());
		
	}
	
	private List<ForeignPaymentOrder> filterByStatus(List<ForeignPaymentOrder> orders, PaymentStatus ... states) {
		List<ForeignPaymentOrder> result = new ArrayList<>();
		List<PaymentStatus> statesList = Arrays.asList(states);
		for (ForeignPaymentOrder order : orders) {
			if (statesList.contains(order.getStatus())) {
				result.add(order);
			}
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private List<ForeignPaymentOrder> loadPendingOrErrorPayments(Session session) {
		return session.createCriteria(ForeignPaymentOrder.class)
			.add(Restrictions.in("status", new PaymentStatus[] {
					PaymentStatus.ERROR,
					PaymentStatus.NEW, 
					PaymentStatus.SENT_TO_WEBDAV, 
					PaymentStatus.WEBDAV_PENDING, 
					PaymentStatus.WEBDAV_ERROR, 
					PaymentStatus.WEBDAV_SIGNED, 
					PaymentStatus.REJECTED
				}))
			.addOrder(Order.asc("updateTimestamp"))
			.list();
	}		

}
