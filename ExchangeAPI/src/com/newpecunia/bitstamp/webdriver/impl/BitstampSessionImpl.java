package com.newpecunia.bitstamp.webdriver.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.newpecunia.bitstamp.service.impl.BitstampCredentials;
import com.newpecunia.bitstamp.webdriver.BitstampSession;
import com.newpecunia.bitstamp.webdriver.BitstampWebdriverException;
import com.newpecunia.net.HttpReader;
import com.newpecunia.net.HttpReaderFactory;

public class BitstampSessionImpl implements BitstampSession {

	private HttpReader httpReader;
	
	private BitstampSessionImpl() {}
	
	//package private
	static BitstampSessionImpl createSession(HttpReaderFactory httpReaderFactory, BitstampCredentials credentials) throws IOException, BitstampWebdriverException {
		BitstampSessionImpl session = new BitstampSessionImpl();
		
		session.httpReader = httpReaderFactory.createNewHttpReaderSession();
		
		session.login(credentials.getUsername(), credentials.getPassword());
		return session;
	}

	private void login(String username, String password) throws IOException, BitstampWebdriverException {
		//navigate to login page
		String loginPage = navigateToLoginPage();
		//parse login form
		Document pageDom = Jsoup.parse(loginPage);
		Element form = pageDom.getElementsByTag("form").get(0);
		Map<String, String> hiddenAttributes = new HashMap<>();
		for (Element input : form.getElementsByTag("input")) {
			if (input.attr("type").equalsIgnoreCase("hidden")) {
				String name = input.attr("name");
				String value = input.attr("value");
				hiddenAttributes.put(name,  value);				
			}
		}
		//perform login
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("password", password));
		for (Entry<String, String> entry : hiddenAttributes.entrySet()) {
			params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		
		List<Header> headers = new ArrayList<>();
		headers.add(new BasicHeader("Referer", BitstampWebdriverConstants.LOGIN_URL));
		String resultPage = httpReader.post(BitstampWebdriverConstants.LOGIN_URL, headers, params);
		
		//TODO verify result
	}

	private String navigateToLoginPage() throws IOException, BitstampWebdriverException {
		String loginPage = httpReader.get(BitstampWebdriverConstants.LOGIN_URL);
		verifyPageContainsText(loginPage, "login page", "<h1>Member Login</h1>", "id_username", "id_password", "login_form");
		return loginPage;
	}

	private void verifyPageContainsText(String page, String pageName, String ... texts) throws BitstampWebdriverException {
		if (page == null) {throw new BitstampWebdriverException("Content of page "+pageName+" is null."); }
		for (String text : texts) {
			if (!page.contains(text)) {
				throw new BitstampWebdriverException("Unable to open "+pageName+".");
			}
		}
	}
	
}
