package com.newpecunia.exchangeweb.serviceservlets;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
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
import com.newpecunia.countries.Country;
import com.newpecunia.countries.CountryDatabase;
import com.newpecunia.email.EmailSender;
import com.newpecunia.trader.service.BuyService;

@Singleton
public class UnconfirmedBuyServlet extends AbstractServiceServlet {
	private static final long serialVersionUID = 1L;
	
	private BuyService buyService;

	private EmailSender emailSender;

	private CountryDatabase countryDatabase;
	
	@Inject
	UnconfirmedBuyServlet(BuyService buyService, EmailSender emailSender, CountryDatabase countryDatabase) {
		this.buyService = buyService;
		this.emailSender = emailSender;
		this.countryDatabase = countryDatabase;
	}	
	
	@Override
	protected void servePost(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		UnconfirmedPreOrderBuyRequest buyRequest = parseAndValidateRequest(req);

		//create unconfirmed-pre-order
		String unconfirmedPreorderId = 
				buyService.createUnconfirmedPreorder(
						buyRequest.getBtcAmount(),
						buyRequest.getBtcAddress(),
						buyRequest.getEmail(),
						buyRequest.getName(),
						buyRequest.getAddress(),
						buyRequest.getCity(),
						buyRequest.getCountry()
						);
		
		//send e-mail
		//TODO
		String link = "http://www.newpecunia.com/buy?confirmation="+unconfirmedPreorderId;
		emailSender.sendHtmlEmail(buyRequest.getEmail(), "Order confirmation", 
				"Thank you for your order. <br><br>\n"+
				"Please continue by clicking on the following link: <a href="+link+">"+link+"</a> <br><br> \n"+
				"Team New Pecunia");
		
		ServletOutputStream out = resp.getOutputStream();
		out.print("emailSent");
	}
	

	private class UnconfirmedPreOrderBuyRequest {
		private BigDecimal btcAmount;
		private String btcAddress;
		private String email;
		private String name;
		private String address;
		private String city;
		private Country country;

		public UnconfirmedPreOrderBuyRequest(BigDecimal btcAmount, String btcAddress, String email, String name, String address, String city, Country country) {
			super();
			this.btcAmount = btcAmount;
			this.btcAddress = btcAddress;
			this.email = email;
			this.name = name;
			this.address = address;
			this.city = city;
			this.country = country;
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

		public Country getCountry() {
			return country;
		}
		
	}	

	
	private UnconfirmedPreOrderBuyRequest parseAndValidateRequest(HttpServletRequest req) {
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
			checkMandatoryField(email);
			try {
			    InternetAddress emailAddr = new InternetAddress(email);
			    emailAddr.validate(); //throws exception when the email is not valid
			} catch (AddressException ae) {
				throw new NPException("Provided e-mail '"+email+"' is invalid", ae);
			}
			
			String name = paramMap.get("inputName");
			checkMandatoryField(name);

			String address = paramMap.get("inputAddress");
			checkMandatoryField(address);

			String city = paramMap.get("inputCity");
			checkMandatoryField(city);

			String countryCode = paramMap.get("inputCountry");
			checkMandatoryField(countryCode);
			Country country = countryDatabase.getCountryForISO(countryCode);
			if (country == null) {
				throw new NPException("Invalid country code '"+countryCode+"'");
			}
			
			
			return new UnconfirmedPreOrderBuyRequest(buyBtcAmount, btcAddress, email, name, address, city, country);
		} catch (Exception e) {
			throw new NPException("The form was incorrectly filled.", e);
		}
	}
	
	
	private void checkMandatoryField(String s) {
		if (StringUtils.isBlank(s)) {throw new NullPointerException("Field is mandatory");}
	}	
}
