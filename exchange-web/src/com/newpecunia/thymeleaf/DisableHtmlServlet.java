package com.newpecunia.thymeleaf;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "disableHtml", description = "Returns 404 whent trying to open *.html page.", urlPatterns = "*.html")
public class DisableHtmlServlet extends HttpServlet {
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
    	response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }	
}
