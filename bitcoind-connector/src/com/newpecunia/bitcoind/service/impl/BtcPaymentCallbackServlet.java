package com.newpecunia.bitcoind.service.impl;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/btcPaymentCallback_dgqac0akerd1c4e7asiy5d8zqjdg68652u") //the address has non-guesable postfix to prevent calling it by some attacker
public class BtcPaymentCallbackServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) {
		try {
			String txId = req.getParameter("txId");
			if (txId == null) {
				res.getOutputStream().write("No transaction ID provided.".getBytes());
				res.setStatus(HttpServletResponse.SC_FORBIDDEN);
			} else {
				//TODO call CallbackManager with the TxID
			}
			
		} catch (IOException e) {
			//do nothing - the output would not be written anywhere - the connection was probably closed
		}
		
	}
	
}
