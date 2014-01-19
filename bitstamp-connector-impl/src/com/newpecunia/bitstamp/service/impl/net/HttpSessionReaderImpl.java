package com.newpecunia.bitstamp.service.impl.net;

import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

public class HttpSessionReaderImpl extends HttpSimpleReaderImpl implements HttpReader {
	
	HttpClient httpClient = null;
	
	//package private constructor -> instantiate it always with Factory
	HttpSessionReaderImpl(RequestCountLimitVerifier requestCountLimitVerifier) {
		super(requestCountLimitVerifier);
		 // set the connection timeout value to 30 seconds (30000 milliseconds)
	    final HttpParams httpParams = new BasicHttpParams();
	    HttpConnectionParams.setConnectionTimeout(httpParams, 30000);
	    HttpConnectionParams.setSoTimeout(httpParams, 30000);
		
		httpClient = new DefaultHttpClient(httpParams);
		CookieStore cookieStore = new BasicCookieStore();
		HttpContext httpContext = new BasicHttpContext();
		httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
	}
	
	@Override
	protected HttpClient getHttpClient() {
		return httpClient;
	}

}
