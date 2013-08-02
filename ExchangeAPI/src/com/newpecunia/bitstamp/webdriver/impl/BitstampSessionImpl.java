package com.newpecunia.bitstamp.webdriver.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.newpecunia.bitstamp.service.impl.BitstampCredentials;
import com.newpecunia.bitstamp.webdriver.BitstampSession;
import com.newpecunia.bitstamp.webdriver.BitstampWebdriverException;
import com.newpecunia.bitstamp.webdriver.InternationalWithdrawRequest;
import com.newpecunia.bitstamp.webdriver.WithdrawOverviewLine;
import com.newpecunia.bitstamp.webdriver.WithdrawOverviewLine.WithdrawStatus;
import com.newpecunia.bitstamp.webdriver.WithdrawOverviewLine.WithdrawType;
import com.newpecunia.net.HttpReader;
import com.newpecunia.net.HttpReaderFactory;
import com.newpecunia.net.HttpReaderOutput;
import com.newpecunia.synchronization.ClusterLockProvider;

public class BitstampSessionImpl implements BitstampSession {
	
	private static final int MINIMUM_DEPOSIT_LIMIT = 50; //minimum deposit in USD
	private static final int MAXIMUM_DEPOSIT_LIMIT = 1000000; //maximum deposit in USD
	private static final BigDecimal MINIMUM_WITHDRAW_LIMIT = new BigDecimal(50);
	private static final BigDecimal MAXIMAL_WITHDRAW_LIMIT = new BigDecimal("99999.99");

	private HttpReader httpReader;
	
	private BitstampSessionImpl() {}
	
	private String lastOpenedUrl = null; //for Referer header
	private ClusterLockProvider lockProvider;
	
	//package private
	static BitstampSessionImpl createSession(HttpReaderFactory httpReaderFactory, BitstampCredentials credentials, ClusterLockProvider lockProvider) throws IOException, BitstampWebdriverException {
		BitstampSessionImpl session = new BitstampSessionImpl();
		
		session.httpReader = httpReaderFactory.createNewHttpSessionReader();
		session.lockProvider = lockProvider;
		session.login(credentials.getUsername(), credentials.getPassword());
		return session;
	}

	private void login(String username, String password) throws IOException, BitstampWebdriverException {
		//navigate to login page
		String loginPage = navigateToLoginPage();
		//parse login form
		Document pageDom = Jsoup.parse(loginPage);
		Element form = pageDom.getElementsByTag("form").get(0);
		LinkedHashMap<String, String> params = getHiddenParamsFromForm(form);
		//add parameters for username and password
		params.put("username", username);
		params.put("password", password);
		//perform login
		post(BitstampWebdriverConstants.LOGIN_URL, params);
	}

	private LinkedHashMap<String, String> getHiddenParamsFromForm(Element form) {
		LinkedHashMap<String, String> hiddenAttributes = new LinkedHashMap<>();
		for (Element input : form.getElementsByTag("input")) {
			if (input.attr("type").equalsIgnoreCase("hidden")) {
				String name = input.attr("name");
				String value = input.attr("value");
				hiddenAttributes.put(name,  value);				
			}
		}
		return hiddenAttributes;
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
	public void createInternationalUSDDeposit(int amount, String firstname, String surname, String comment) throws IOException, BitstampWebdriverException {
		if (amount < MINIMUM_DEPOSIT_LIMIT) {
			throw new BitstampWebdriverException("Deposit amount must be at lease "+MINIMUM_DEPOSIT_LIMIT+" USD.");
		}
		if (amount > MAXIMUM_DEPOSIT_LIMIT) {
			throw new BitstampWebdriverException("Deposit amount must be maximally "+MAXIMUM_DEPOSIT_LIMIT+" USD.");
		}
		
		//navigate to the international deposit page
		String url = BitstampWebdriverConstants.INTERNATIONAL_DEPOSIT_URL;
		String page = get(url);
		//verify whether deposit is possible - the Bitstamp is not waiting for another deposit.
		if (page.toUpperCase().contains("WAITING FOR YOU TO SEND THE FUNDS...")) {
			throw new BitstampWebdriverException("In status of waiting for a deposit. Either wait until the deposit is finished or cancel the request.");
		}
		verifyPageContainsText(page, url, "INTERNATIONAL WIRE TRANSFER");
		
		//parse international deposit form
		Document pageDom = Jsoup.parse(page);
		Element form = pageDom.getElementsByTag("form").get(0);
		LinkedHashMap<String, String> params = getHiddenParamsFromForm(form);
		//add parameters of the deposit form
		params.put("first_name", firstname);
		params.put("last_name", surname);
		params.put("amount", ""+amount);
		params.put("currency", "USD");
		params.put("comment", comment);
		//perform deposit
		post(url, params);
		
		//verify that Bitstamp is now waiting for deposit
		if (!isWaitingForDeposit()) {
			throw new BitstampWebdriverException("Some error ocurred while creating deposit - Bitstamp is NOT in status \"waiting for deposit\".");
		}
	}

	@Override
	public void cancelLastDeposit() throws IOException, BitstampWebdriverException {
		String url = BitstampWebdriverConstants.CANCEL_URL;
		String page = get(url);
		verifyPageContainsText(page, url, "YOUR DEPOSIT REQUESTS");
	}

	@Override
	public List<WithdrawOverviewLine> getWithdrawOverview() throws IOException, BitstampWebdriverException {
		String withrawOverviewUrl = BitstampWebdriverConstants.WITHDRAW_URL;
		String withdrawOverviewPage = get(withrawOverviewUrl);
		verifyPageContainsText(withdrawOverviewPage, withrawOverviewUrl, "YOUR WITHDRAWAL REQUESTS");
		
		//parse international deposit form
		Document pageDom = Jsoup.parse(withdrawOverviewPage);
		Element table = pageDom.getElementsByTag("table").get(0);
		Elements rows = table.getElementsByTag("tr");
		List<WithdrawOverviewLine> lines = new ArrayList<>();
		boolean header = true;
		for (Element row : rows) {
			if (header) { //skip over header
				header = false;
				continue;
			}
			
			Elements cells = row.getElementsByTag("td");
			WithdrawOverviewLine line = WithdrawOverviewLineParser.parseRowCells(cells);
			lines.add(line);
		}
		
		return lines;
	}
	
	@Override
	public Long createInternationalWithdraw(InternationalWithdrawRequest request) throws IOException, BitstampWebdriverException {
		verifyWithdrawRequest(request);

		Lock lock = lockProvider.getLock();		
		try {
			lock.lock();
			
			List<WithdrawOverviewLine> overviewBefore = getWithdrawOverview();
			
			//navigate to the international withdraw page
			String url = BitstampWebdriverConstants.INTERNATIONAL_WITHDRAW_URL;
			String page = get(url);
			verifyPageContainsText(page, url, "INTERNATIONAL WIRE TRANSFER", "Withdraw");
			
			//parse international withdraw form
			Document pageDom = Jsoup.parse(page);
			Element form = pageDom.getElementsByTag("form").get(0);
			LinkedHashMap<String, String> params = getHiddenParamsFromForm(form);
			//add parameters of the withdraw form
			params.put("name", request.getName());
			params.put("amount", request.getAmount().toPlainString());
			params.put("currency", request.getCurrency());
			params.put("address", request.getAddress());
			params.put("postal_code", request.getPostalCode());
			params.put("city", request.getCity());
			params.put("country", request.getCountry());
			params.put("iban", request.getIban());
			params.put("bic", request.getBic());
			params.put("bank_name", request.getBankName());
			params.put("bank_address", request.getBankAddress());
			params.put("bank_postal_code", request.getBankPostalCode());
			params.put("bank_city", request.getBankCity());
			params.put("bank_country", request.getBankCountry());
			params.put("comment", request.getComment() == null ? "" : request.getComment());
			
			//perform withdraw
			post(url, params);		
			
			//verify that the withdrawal was successful			
			List<WithdrawOverviewLine> overviewAfter = getWithdrawOverview();
			//find last international withdraw
			Long lastInternationalWithdrawId = null;
			for (WithdrawOverviewLine lineBefore : overviewBefore) {
				if (WithdrawType.INTERNATIONAL_BANK_TRANSFER.equals(lineBefore.getWithdrawType())) {
					lastInternationalWithdrawId = lineBefore.getId();
					break;
				}
			}
			
			WithdrawOverviewLine newWithdrawLine = null;
			for (WithdrawOverviewLine lineAfter : overviewAfter) {
				if ((new Long(lineAfter.getId())).equals(lastInternationalWithdrawId)) {
					break;
				}
				if (WithdrawType.INTERNATIONAL_BANK_TRANSFER.equals(lineAfter.getWithdrawType())
						&& WithdrawStatus.WAITING_FOR_CONFIRMATION.equals(lineAfter.getStatus())
						&& lineAfter.getAmount().compareTo(request.getAmount()) == 0) {
					newWithdrawLine = lineAfter;
					break;
				}
			}
			
			if (newWithdrawLine == null) {
				throw new BitstampWebdriverException("Creating withdraw request was not successful. (The new withdraw request was not found in the overview).");
			}
			
			return newWithdrawLine.getId();
		} finally {
			lock.unlock();
		}
	}
	
	private void verifyWithdrawRequest(InternationalWithdrawRequest request) throws BitstampWebdriverException {
		if (request.getAmount() == null) {throw new BitstampWebdriverException("Amount is mandatory.");}
		if (request.getAmount().compareTo(MINIMUM_WITHDRAW_LIMIT) < 0) {throw new BitstampWebdriverException("Amount must be at least 50.");}
		if (request.getAmount().compareTo(MAXIMAL_WITHDRAW_LIMIT) > 0) {throw new BitstampWebdriverException("Amount must be maximally 99999.99.");}
		if (request.getAmount().scale() > 2) {throw new BitstampWebdriverException("Amount can have maximally two digits after the decimal point.");}
		if (StringUtils.isEmpty(request.getCurrency())) {throw new BitstampWebdriverException("Currency is mandatory.");};
			
		if (StringUtils.isEmpty(request.getBic())) {throw new BitstampWebdriverException("BIC is mandatory.");}
		if (StringUtils.isEmpty(request.getIban())) {throw new BitstampWebdriverException("IBAN is mandatory.");}
		
		if (StringUtils.isEmpty(request.getBankName())) {throw new BitstampWebdriverException("Bank name is mandatory.");}
		if (StringUtils.isEmpty(request.getBankAddress())) {throw new BitstampWebdriverException("Bank address is mandatory.");}
		if (StringUtils.isEmpty(request.getBankCity())) {throw new BitstampWebdriverException("Bank city is mandatory.");}
		if (StringUtils.isEmpty(request.getBankPostalCode())) {throw new BitstampWebdriverException("Bank postal code is mandatory.");}
		if (StringUtils.isEmpty(request.getBankCountry())) {throw new BitstampWebdriverException("Bank country is mandatory.");}

		if (StringUtils.isEmpty(request.getName())) {throw new BitstampWebdriverException("Name is mandatory.");}
		if (StringUtils.isEmpty(request.getAddress())) {throw new BitstampWebdriverException("Address is mandatory.");}
		if (StringUtils.isEmpty(request.getCity())) {throw new BitstampWebdriverException("City is mandatory.");}
		if (StringUtils.isEmpty(request.getPostalCode())) {throw new BitstampWebdriverException("Postal code is mandatory.");}
		if (StringUtils.isEmpty(request.getCountry())) {throw new BitstampWebdriverException("Country is mandatory.");}
	}

	
	@Override
	public void cancelWithdraw(long id) throws IOException, BitstampWebdriverException {
		List<WithdrawOverviewLine> overview = getWithdrawOverview();
		String cancelUrl = null;
		for (WithdrawOverviewLine line : overview) {
			if (id == line.getId()) {
				cancelUrl = line.getCancelUrl();
				break;
			}
		}
		
		if (cancelUrl == null) {
			throw new BitstampWebdriverException("Cannot find withdraw request with ID '"+id+"'. Cannot cancel.");
		}
		
		//cancel the withdraw
		get(BitstampWebdriverConstants.WEB_URL+cancelUrl);
		
		//verify that the withdraw was really canceled
		List<WithdrawOverviewLine> overviewAfterCancel = getWithdrawOverview();
		WithdrawOverviewLine canceledLine = null;
		for (WithdrawOverviewLine line : overviewAfterCancel) {
			if (id == line.getId()) {
				canceledLine = line;
				break;
			}
		}
		if (canceledLine == null) {
			throw new BitstampWebdriverException("Canceled withdraw was not found in overview.");
		}
		if (!WithdrawStatus.CANCELED.equals(canceledLine.getStatus())) {
			throw new BitstampWebdriverException("Canceled withdraw is not in status 'Canceled'.");
		}
		
	}
	
	private String get(String url) throws IOException, BitstampWebdriverException {
		List<Header> headers = new ArrayList<>();
		headers.add(new BasicHeader("Referer", lastOpenedUrl));		
		HttpReaderOutput output = httpReader.getWithMetadata(url, headers);
		verifyResultCode(output.getResultCode(), url);
		lastOpenedUrl = url;
		return output.getOutput();
	}
	
	private String post(String url, LinkedHashMap<String, String> params) throws IOException, BitstampWebdriverException {
		List<Header> headers = new ArrayList<>();
		if (lastOpenedUrl != null) {
			headers.add(new BasicHeader("Referer", lastOpenedUrl));
		}
		List<NameValuePair> paramList = new ArrayList<>(); 
		for (Entry<String, String> entry : params.entrySet()) {
			paramList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}		
		HttpReaderOutput result = httpReader.postWithMetadata(url, headers, paramList);
		verifyResultCode(result.getResultCode(), url);
		lastOpenedUrl = url;
		return result.getOutput();
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
