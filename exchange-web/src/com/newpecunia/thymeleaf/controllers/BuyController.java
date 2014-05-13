package com.newpecunia.thymeleaf.controllers;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import com.google.inject.Inject;
import com.newpecunia.configuration.NPConfiguration;
import com.newpecunia.countries.Country;
import com.newpecunia.countries.CountryDatabase;
import com.newpecunia.time.TimeProvider;
import com.newpecunia.trader.service.BuyService;
import com.newpecunia.trader.service.UnconfirmedBuyPreOrder;

public class BuyController implements ThymeleafController {

	public static final String SESSION_ATTR_UNCONFIRMED_PREORDER_ID = "unconfirmedPreorderId";

	private TimeProvider timeProvider;
	private NPConfiguration configuration;
	private BuyService buyService;
	private CountryDatabase countryDB;

	@Inject
	BuyController(BuyService buyService, TimeProvider timeProvider, NPConfiguration configuration, CountryDatabase countryDB) {
		this.buyService = buyService;
		this.timeProvider = timeProvider;
		this.configuration = configuration;
		this.countryDB = countryDB;
	}
	
	@Override
	public String process(HttpServletRequest request,
			HttpServletResponse response, ServletContext servletContext,
			TemplateEngine templateEngine) {

		Map<String, Object> variables = new HashMap<>();
		initCountries(variables);
		
		String confirmationId = request.getParameter("confirmation");
		boolean confirmationProvided = !StringUtils.isEmpty(confirmationId);
		boolean confirmationValid = false;
		UnconfirmedBuyPreOrder unconfPreorder = null;
		if (confirmationProvided) {
			unconfPreorder = getUnconfirmedPreorder(confirmationId);
			confirmationValid = checkUnconfirmedPreorderValidity(unconfPreorder);
			variables.put("confirmationValid", confirmationValid);
		}
		
		if (confirmationProvided && confirmationValid) {
			request.getSession().setAttribute(SESSION_ATTR_UNCONFIRMED_PREORDER_ID, confirmationId);
			variables.put("email", unconfPreorder.getEmail());
			variables.put("name", unconfPreorder.getName());
			variables.put("address", unconfPreorder.getAddress());
			variables.put("city", unconfPreorder.getCity());
			variables.put("countries", Arrays.asList(countryDB.getCountryForISO(unconfPreorder.getCountryCode())));
			variables.put("btcAddress", unconfPreorder.getBtcAddress());
			variables.put("btcAmount", unconfPreorder.getBtcAmount().toPlainString());
			initCardProcessingUrl(variables);			
		}
        
		WebContext ctx = new WebContext(request, response, servletContext, request.getLocale(), variables);
        return templateEngine.process("/buy/buy.html", ctx);
	}

	private boolean checkUnconfirmedPreorderValidity(UnconfirmedBuyPreOrder unconfirmedPreorder) {
		return true; //we allow the preorder to be infinitely valid
		//uncomment the following code if you need time restricted validity 

//		int UNCONF_PREORDER_VALIDITY_IN_MS = 60*60*24*1000; //24 hours - TODO make configurable
//		if (unconfirmedPreorder == null) {return false;}
//		return (timeProvider.nowCalendar().getTimeInMillis() - unconfirmedPreorder.getCreateTimestamp().getTimeInMillis() 
//				< UNCONF_PREORDER_VALIDITY_IN_MS);
	}
	
	private UnconfirmedBuyPreOrder getUnconfirmedPreorder(String id) {
		return buyService.getUnconfirmedPreorder(id);
	}

	private void initCardProcessingUrl(Map<String, Object> variables) {
		variables.put("cardProcessingUrl", configuration.getCardProcessingUrl());
	}

	private void initCountries(Map<String, Object> variables) {
		List<Country> countries = countryDB.getListOfCountries();
		Collections.sort(countries, new Comparator<Country>(){
			@Override
			public int compare(Country country1, Country country2) {
				return country1.getEnglishName().compareTo(country2.getEnglishName());
			}});
		countries.add(0, new Country("", "Select country..."));
		variables.put("countries", countries);
	}	
}
