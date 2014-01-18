package com.newpecunia.thymeleaf.controllers;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;

public interface ThymeleafController {

    public String process(HttpServletRequest request, HttpServletResponse response, 
    		ServletContext servletContext, TemplateEngine templateEngine);
}
