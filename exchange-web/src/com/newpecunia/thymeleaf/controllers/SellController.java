package com.newpecunia.thymeleaf.controllers;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

public class SellController implements ThymeleafController {

	@Override
	public String process(HttpServletRequest request,
			HttpServletResponse response, ServletContext servletContext,
			TemplateEngine templateEngine) {

        WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        return templateEngine.process("/sell/sell.html", ctx);
	}
}
