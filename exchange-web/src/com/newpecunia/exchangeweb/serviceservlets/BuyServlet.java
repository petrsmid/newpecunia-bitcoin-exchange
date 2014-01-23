package com.newpecunia.exchangeweb.serviceservlets;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import com.google.common.base.Charsets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.newpecunia.NPException;
import com.newpecunia.creditcard.CreditCardAcquiringService;
import com.newpecunia.trader.service.BuyService;
import com.newpecunia.trader.service.TraderService;

@Singleton
public class BuyServlet extends AbstractServiceServlet {
	private static final long serialVersionUID = 1L;
	
	private BuyService buyService;
	private TraderService traderService;
	private CreditCardAcquiringService cardService;

	@Inject
	BuyServlet(BuyService buyService, TraderService traderService, CreditCardAcquiringService cardService) {
		this.buyService = buyService;
		this.traderService = traderService;
		this.cardService = cardService;
	}

	
	@Override
	protected void servePost(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		//get the actual buyPrice as soon as possible - because it changes in time
		BigDecimal buyPrice = traderService.getCustomerBtcBuyPriceInUSD();
		
		BuyRequest buyRequest = parseAndValidateRequest(req);

		//calculate price
		BigDecimal usdAmount = buyRequest.getBtcAmount().multiply(buyPrice);
		
		//call card service and retrieve transaction ID
		String txId = cardService.startCardPaymentTransaction(usdAmount);
		
		//create pre-order
		buyService.createPreOrder(buyRequest.getBtcAmount(), usdAmount, buyRequest.getBtcAddress(), buyRequest.getEmail(), txId);
		
		ServletOutputStream out = resp.getOutputStream();
		out.println("{ \"transactionId\" : \""+txId+"\" }");

	}

	private class BuyRequest {
		private BigDecimal btcAmount;
		private String btcAddress;
		private String email;

		public BuyRequest(BigDecimal btcAmount, String btcAddress, String email) {
			super();
			this.btcAmount = btcAmount;
			this.btcAddress = btcAddress;
			this.email = email;
		}
		
		public BigDecimal getBtcAmount() {
			return btcAmount;
		}
		public String getBtcAddress() {
			return btcAddress;
		}
		public String getEmail() {
			return email;
		}
	}
	
	
	private BuyRequest parseAndValidateRequest(HttpServletRequest req) {
		try {
			String request = IOUtils.toString(req.getInputStream());
			List<NameValuePair> params = URLEncodedUtils.parse(request, Charsets.UTF_8);
			Map<String, String> paramMap = new HashMap<>();
			for (NameValuePair param : params) {
				paramMap.put(param.getName(), param.getValue());
			}
			
			
			String strBuyBtcAmount = paramMap.get("inputBuyAmount");
			checkMandatoryField(strBuyBtcAmount);
			BigDecimal buyBtcAmount = new BigDecimal(strBuyBtcAmount); //verifies the number format as well
			if (buyBtcAmount.compareTo(new BigDecimal("0.01")) < 0) {throw new NPException("Minimum to buy is 0.01 BTCs.");}
			if (buyBtcAmount.compareTo(new BigDecimal(10000)) > 0) {throw new NPException("Maximally 10000 BTCs is allowed to buy.");}
			
			String btcAddress = paramMap.get("inputBtcAddress");
			checkMandatoryField(btcAddress);
			if (!btcAddress.matches("^[a-zA-Z0-9]*$")) {
				throw new NPException("The address '"+btcAddress+"' does not match aphanumeric string pattern.");
			}
			
			String email = paramMap.get("inputEmail");
			
			return new BuyRequest(buyBtcAmount, btcAddress, email);
		} catch (Exception e) {
			throw new NPException("The form was incorrectly filled.", e);
		}
	}
	
	private void checkMandatoryField(String s) {
		if (StringUtils.isBlank(s)) {throw new NullPointerException("Field is mandatory");}
	}
}
