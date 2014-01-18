package com.newpecunia.exchangeweb.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "welcomePageRedirect", description = "Redirects / -> /buy/", urlPatterns = "")
public class WelcomePageRedirectServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doService(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doService(request, response);
    }

    protected void doService(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.sendRedirect("buy/");
    }	
}
