package com.newpecunia.bitcoind.service.impl;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;


/**
 * Servlet for receiving callbacks from bitcoind when some money was received.
 * Call the servlet with parameter txId - this is the transaction ID of the bitcoin payment 
 */
@Singleton
public class BitcoindReceivedPaymentCallbackServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LogManager.getLogger(BitcoindReceivedPaymentCallbackServlet.class);	
	
	@Inject
	private ReceiveBTCCallback callback = null;	
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) {
			String txId = req.getParameter("txId");
			if (txId == null) {
				logger.warn("Servlet for Bitcoind callbacks called without transaction number.");
				res.setStatus(HttpServletResponse.SC_FORBIDDEN);
				try {
					res.getOutputStream().write("No transaction ID provided.".getBytes());
				} catch (IOException e) {
					//do nothing - the connection was probably closed
				}
			} else {
				try {
					logger.info("Servlet for Bitcoind callbacks called with transaction ID "+txId);
					callback.serve(txId);
				} catch (Exception e) {
					logger.error("Error ocurred while processing callback by receiving Bitcoins. Transaction ID: "+txId, e);
				}
			}
			
		
	}
	
}
