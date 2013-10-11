package com.newpecunia.test;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.newpecunia.unicredit.service.ForeignPayment;
import com.newpecunia.unicredit.service.PaymentService;

/**
 * This servlet is only ment for test purposes.
 * It must be removed before going to production!!! 
 */
@Singleton
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Inject
	private PaymentService paymentService;
	
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ForeignPayment preOrder = new ForeignPayment();
		preOrder.setName("Test User 2");
		paymentService.createPreOrderWaitingForBTC(preOrder, "testAddress 2");
	}
}
