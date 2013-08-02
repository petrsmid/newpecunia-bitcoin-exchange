package com.newpecunia.bitstamp.webdriver.impl;

import java.math.BigDecimal;

import org.jsoup.select.Elements;

import com.google.common.base.CharMatcher;
import com.newpecunia.bitstamp.webdriver.BitstampWebdriverException;
import com.newpecunia.bitstamp.webdriver.WithdrawOverviewLine;
import com.newpecunia.bitstamp.webdriver.WithdrawOverviewLine.WithdrawStatus;
import com.newpecunia.bitstamp.webdriver.WithdrawOverviewLine.WithdrawType;

public class WithdrawOverviewLineParser {

	public static WithdrawOverviewLine parseRowCells(Elements lineCells) throws BitstampWebdriverException {
		if (lineCells.size() < 6) {
			throw new BitstampWebdriverException("Unexpected number of cells in a row of withdraw overview.");				
		}
		
		Elements urls = lineCells.get(5).getElementsByTag("a");
		String cancelUrl = null;
		if (urls != null && urls.size() > 0) {
			String urlText = urls.get(0).text();
			if (urlText.toUpperCase().equals("CANCEL")) {
				cancelUrl = urls.get(0).attr("href");
			}
		}
		
		return parseRowCells(lineCells.get(0).text(), lineCells.get(1).text(), lineCells.get(2).text(), lineCells.get(3).text(), lineCells.get(4).text(), cancelUrl);
	}

	
	public static WithdrawOverviewLine parseRowCells(String id, String date, String description, String amount, String status, String cancelUrl) throws BitstampWebdriverException {
		WithdrawOverviewLine line = new WithdrawOverviewLine();
		line.setId(parseId(id));
		line.setDate(date);
		line.setDescription(description);
		line.setAmount(parseAmount(amount));
		line.setStatus(parseStatus(status));
		line.setWithdrawType(parseDescription(description));
		line.setCancelUrl(cancelUrl);
		return line;
	}

	private static WithdrawStatus parseStatus(String status) throws BitstampWebdriverException {
		if (status.toUpperCase().contains("CONFIRMATION NEEDED")) { //E-mail confirmation needed
			return WithdrawStatus.WAITING_FOR_CONFIRMATION;
		} else if (status.toUpperCase().contains("CANCELED")) {
			return WithdrawStatus.CANCELED;
		} else if (status.toUpperCase().contains("FINISHED")) {
			return WithdrawStatus.FINISHED;
		} else {
			throw new BitstampWebdriverException("Cannot parse status '"+status+"'");
		}
	}

	private static long parseId(String id) throws BitstampWebdriverException {
		try {
			return Long.parseLong(id);
		} catch (NumberFormatException e) {
			throw new BitstampWebdriverException("Cannot parse number ID from srting '"+id+"'", e);
		}
	}

	private static WithdrawType parseDescription(String description) throws BitstampWebdriverException {
		String descUppercase = description.toUpperCase();
		if (descUppercase.contains("BITCOIN")) {
			return WithdrawType.BITCOIN;
		} else if (descUppercase.contains("INTERNATIONAL")) {
			return WithdrawType.INTERNATIONAL_BANK_TRANSFER;
		} else {
			throw new BitstampWebdriverException("Cannot determine withdraw type from string '"+description+"'");
		}
	}

	private static BigDecimal parseAmount(String amount) {
		CharMatcher numberFilter = CharMatcher.inRange('0',  '9').or(CharMatcher.is('.'));
		String amountNumber = numberFilter.retainFrom(amount);
		return new BigDecimal(amountNumber);
	}


}
