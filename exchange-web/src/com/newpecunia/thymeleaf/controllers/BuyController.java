package com.newpecunia.thymeleaf.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import com.google.inject.Inject;
import com.newpecunia.time.TimeProvider;

public class BuyController implements ThymeleafController {

	private TimeProvider timeProvider;

	@Inject
	BuyController(TimeProvider timeProvider) {
		this.timeProvider = timeProvider;
	}
	
	@Override
	public String process(HttpServletRequest request,
			HttpServletResponse response, ServletContext servletContext,
			TemplateEngine templateEngine) {

        Map<String, Object> variables = new HashMap<>();
        
        initExpirationYears(variables);
        
		WebContext ctx = new WebContext(request, response, servletContext, request.getLocale(), variables);
        return templateEngine.process("/buy/buy.html", ctx);
	}

	private void initExpirationYears(Map<String, Object> variables) {
		List<String> years = new ArrayList<>();
		int actualYear = timeProvider.nowDateTime().getYear();
		for (int i = actualYear; i <= actualYear + 20; i++) {
			years.add(""+i);
		}
		variables.put("years", years);
	}
}
