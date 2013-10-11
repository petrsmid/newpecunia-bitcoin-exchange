package com.newpecunia.guice;

import javax.servlet.annotation.WebFilter;

import com.google.inject.servlet.GuiceFilter;

/**
 * This filter just encapsulates the original GuiceFilter
 * with annotation @WebFilter just to enable starting it
 * with Servlet API 3.0 without editing web.xml
 */
@WebFilter(urlPatterns="/*")
public class NPGuiceFilter extends GuiceFilter {

}
