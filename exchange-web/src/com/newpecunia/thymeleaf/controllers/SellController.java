package com.newpecunia.thymeleaf.controllers;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import com.google.inject.Inject;
import com.newpecunia.countries.Country;
import com.newpecunia.countries.CountryDatabase;

public class SellController implements ThymeleafController {

	private CountryDatabase countryDB;

	@Inject
	SellController(CountryDatabase countryDB) {
		this.countryDB = countryDB;
	}
	
	@Override
	public String process(HttpServletRequest request,
			HttpServletResponse response, ServletContext servletContext,
			TemplateEngine templateEngine) {

        Map<String, Object> variables = new HashMap<>();
        
        initCountries(variables);
        
        
		WebContext ctx = new WebContext(request, response, servletContext, request.getLocale(), variables);
        return templateEngine.process("/sell/sell.html", ctx);
	}

	private void initCountries(Map<String, Object> variables) {
		List<Country> countries = countryDB.getListOfCountries();
		Collections.sort(countries, new Comparator<Country>(){
			@Override
			public int compare(Country country1, Country country2) {
				return country1.getEnglishName().compareTo(country2.getEnglishName());
			}});
		variables.put("countries", countries);
	}
}
