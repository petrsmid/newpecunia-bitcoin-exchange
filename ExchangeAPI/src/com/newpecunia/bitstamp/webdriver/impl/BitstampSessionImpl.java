package com.newpecunia.bitstamp.webdriver.impl;

import java.io.IOException;
import java.math.BigDecimal;
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
import com.newpecunia.net.HttpReaderOutput;

public class BitstampSessionImpl implements BitstampSession {

	private HttpReader httpReader;
	
	private BitstampSessionImpl() {}
	
	private String lastOpenedUrl = null; //for Referer header
	
	//package private
	static BitstampSessionImpl createSession(HttpReaderFactory httpReaderFactory, BitstampCredentials credentials) throws IOException, BitstampWebdriverException {
		BitstampSessionImpl session = new BitstampSessionImpl();
		
		session.httpReader = httpReaderFactory.createNewHttpSessionReader();
		
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
		headers.add(new BasicHeader("Referer", lastOpenedUrl));
		HttpReaderOutput result = httpReader.postWithMetadata(BitstampWebdriverConstants.LOGIN_URL, headers, params);
		
		verifyResultCode(result.getResultCode(), BitstampWebdriverConstants.LOGIN_URL);
		lastOpenedUrl = BitstampWebdriverConstants.LOGIN_URL;
	}

	private String navigateToLoginPage() throws IOException, BitstampWebdriverException {
		String url = BitstampWebdriverConstants.LOGIN_URL;
		HttpReaderOutput loginPageOutput = httpReader.getWithMetadata(url);
		verifyResultCode(loginPageOutput.getResultCode(), url);
		lastOpenedUrl = url;
		verifyPageContainsText(loginPageOutput.getOutput(), url,
				"<h1>Member Login</h1>", "id_username", "id_password", "login_form");
		return loginPageOutput.getOutput();
	}

	@Override
	public boolean isWaitingForDeposit() throws IOException, BitstampWebdriverException {
		String url = BitstampWebdriverConstants.DEPOSIT_URL;
		String page = get(url);
		verifyPageContainsText(page, url, "Deposit");
		if (page.toUpperCase().contains("WAITING FOR YOU TO SEND THE FUNDS...")) {
			return true;
		} else if (page.toUpperCase().contains("YOUR DEPOSIT REQUESTS")){
			return false;
		} else {
			throw new BitstampWebdriverException("Cannot determine state of deposit waiting.");
		}
	}
	
	@Override
	public void createInternationalUSDDeposit(BigDecimal amount, String name, String surname, String comment) throws IOException, BitstampWebdriverException {
		//navigate to the international deposit page
		String url = BitstampWebdriverConstants.INTERNATIONAL_DEPOSIT_URL;
		String page = get(url);
		//verify whether deposit is possible - the Bitstamp is not waiting for another deposit.
		if (page.toUpperCase().contains("WAITING FOR YOU TO SEND THE FUNDS...")) {
			throw new BitstampWebdriverException("In status of waiting for a deposit. Either wait until the deposit is finished or cancel the request.");
		}
		verifyPageContainsText(page, url, "INTERNATIONAL WIRE TRANSFER");
		
		//TODO implement me
		
	}

	private String get(String url) throws IOException, BitstampWebdriverException {
		List<Header> headers = new ArrayList<>();
		headers.add(new BasicHeader("Referer", lastOpenedUrl));		
		HttpReaderOutput output = httpReader.getWithMetadata(url, headers);
		verifyResultCode(output.getResultCode(), url);
		lastOpenedUrl = url;
		return output.getOutput();
	}
	
	
	
	private void verifyPageContainsText(String page, String pageUrl, String ... texts) throws BitstampWebdriverException {
		if (page == null) {throw new BitstampWebdriverException("Content of page "+pageUrl+" is null."); }
		String pageUpperCase = page.toUpperCase();
		for (String text : texts) {
			if (!pageUpperCase.contains(text.toUpperCase())) {
				throw new BitstampWebdriverException("Unable to open "+pageUrl+".");
			}
		}
	}
	
	private void verifyResultCode(int code, String pageUrl) throws BitstampWebdriverException {
		if (code >= 400) {
			throw new BitstampWebdriverException("Error "+code+" returned when opening page "+pageUrl+".");
		}
	}
	
}
