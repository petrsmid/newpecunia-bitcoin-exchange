package com.newpecunia.net;

import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

public class HttpSessionReaderImpl extends HttpSimpleReaderImpl implements HttpReader {
	
	HttpClient httpClient = null;
	
	//package private constructor -> instantiate it always with Factory
	HttpSessionReaderImpl() {
		httpClient = new DefaultHttpClient();
		CookieStore cookieStore = new BasicCookieStore();
		HttpContext httpContext = new BasicHttpContext();
		httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
	}
	
	@Override
	protected HttpClient getHttpClient() {
		return httpClient;
	}

}
