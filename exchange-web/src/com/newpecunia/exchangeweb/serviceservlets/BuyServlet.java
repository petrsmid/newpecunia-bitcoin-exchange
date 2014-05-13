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
import com.newpecunia.thymeleaf.controllers.BuyController;
import com.newpecunia.trader.service.BuyService;
import com.newpecunia.trader.service.TraderService;
import com.newpecunia.trader.service.UnconfirmedBuyPreOrder;

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
		ServletOutputStream out = resp.getOutputStream();

		//get the actual buyPrice as soon as possible - because it changes in time
		BigDecimal buyPrice = traderService.getCustomerBtcBuyPriceInUSD();

		//read unconfirmed preorder ID from the session
		String unconfirmedPreorderId = (String) req.getSession().getAttribute(BuyController.SESSION_ATTR_UNCONFIRMED_PREORDER_ID);
		if (unconfirmedPreorderId == null) {
			out.println("sessionExpired"); //TODO show warning in the buy.html
			return;
		}		
		
		//parse the request
		String request = IOUtils.toString(req.getInputStream());
		BuyRequest buyRequest = parseAndValidateRequest(request, unconfirmedPreorderId);

		//calculate price
		BigDecimal usdAmount = buyRequest.getBtcAmount().multiply(buyPrice);
		
		//call card service and retrieve transaction ID
		String txId = cardService.startCardPaymentTransaction(usdAmount);
		
		//create pre-order
		buyService.createPreOrder(buyRequest.getBtcAmount(), 
				usdAmount, 
				buyRequest.getBtcAddress(), 
				buyRequest.getEmail(), 
				buyRequest.getName(), 
				buyRequest.getAddress(), 
				buyRequest.getCity(), 
				buyRequest.getCountryCode(), 
				txId);
		
		out.print("{ \"transactionId\" : \""+txId+"\" }");

	}

	private class BuyRequest {
		private BigDecimal btcAmount;
		private String btcAddress;
		private String email;
		private String name;
		private String address;
		private String city;
		private String countryCode;

		public BuyRequest(BigDecimal btcAmount, String btcAddress, String email, String name, String address, String city, String countryCode) {
			super();
			this.btcAmount = btcAmount;
			this.btcAddress = btcAddress;
			this.email = email;
			this.name = name;
			this.address = address;
			this.city = city;
			this.countryCode = countryCode;
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

		public String getName() {
			return name;
		}

		public String getAddress() {
			return address;
		}

		public String getCity() {
			return city;
		}

		public String getCountryCode() {
			return countryCode;
		}
	}
	
	
	private BuyRequest parseAndValidateRequest(String request, String unconfirmedPreorderId) {
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
		
		UnconfirmedBuyPreOrder unconfirmedPreorder = buyService.getUnconfirmedPreorder(unconfirmedPreorderId);
		
		return new BuyRequest(buyBtcAmount, 
				unconfirmedPreorder.getBtcAddress(), 
				unconfirmedPreorder.getEmail(),
				unconfirmedPreorder.getName(),
				unconfirmedPreorder.getAddress(),
				unconfirmedPreorder.getCity(),
				unconfirmedPreorder.getCountryCode()
				);
	}
	
	private void checkMandatoryField(String s) {
		if (StringUtils.isBlank(s)) {throw new NullPointerException("Field is mandatory");}
	}
}
