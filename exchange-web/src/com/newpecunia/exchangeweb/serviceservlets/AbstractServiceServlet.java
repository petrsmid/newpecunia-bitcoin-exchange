package com.newpecunia.exchangeweb.serviceservlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractServiceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LogManager.getLogger(AbstractServiceServlet.class);	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
	        resp.setContentType("text/html;charset=UTF-8");
	        resp.setHeader("Pragma", "no-cache");
	        resp.setHeader("Cache-Control", "no-cache");
	        resp.setDateHeader("Expires", 0);

	        serveGet(req, resp);
		} catch (Exception e) {
			logger.error(e, e);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
	        resp.setContentType("text/html;charset=UTF-8");
	        resp.setHeader("Pragma", "no-cache");
	        resp.setHeader("Cache-Control", "no-cache");
	        resp.setDateHeader("Expires", 0);
	        
			servePost(req, resp);
		} catch (Exception e) {
			logger.error(e, e);
		}
	}

	protected void serveGet(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		super.doGet(req, resp);
	}

	protected void servePost(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		super.doPost(req, resp);
	}

}
