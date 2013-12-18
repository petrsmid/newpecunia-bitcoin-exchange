package com.newpecunia.unicredit.webdav.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.newpecunia.configuration.NPConfiguration;

@Singleton
public class StructuredMulticashStatementParser {
	
	private static final Logger logger = LogManager.getLogger(StructuredMulticashStatementParser.class);	
	
	
	private static final String PAYMENT_REFERENCE_PATTERN = "Pecunia X(........)X"; //Must correspond with MulticashForeignPaymentPackage.TRANSACTION_TEXT_FORMAT_FOR_CUSTOMER
	
	private NPConfiguration configuration;

	
	@Inject
	StructuredMulticashStatementParser(NPConfiguration configuration) {
		this.configuration = configuration;
	}
	
	public Statement parseStatements(String inputFile) {
		logger.trace("Parsing statement file: "+inputFile);
		Statement result = new Statement();
		result.setFoundNonBitstampPaymentReferences(findNonBitstampPaymentReferences(inputFile));
		result.setBitstampReferencesCount(getBitstampPaymentReferencesCount(inputFile));
		result.setBalance(parseBalance(inputFile));
		return result;
	}

	private List<String> findNonBitstampPaymentReferences(String inputFile) {
		List<String> foundReferences = new ArrayList<>();
		//New Pecunia X1KJVDUJIX where the 1KJVDUJI is the ID in Base32 and the 1 is the version of the ID generator
		Pattern pattern = Pattern.compile(PAYMENT_REFERENCE_PATTERN);
		Matcher matcher = pattern.matcher(inputFile);
		while(matcher.find()) {
			foundReferences.add(matcher.group(1));
		}
		return foundReferences;
	}
	
	private int getBitstampPaymentReferencesCount(String inputFile) {
		Pattern pattern = Pattern.compile(configuration.getBitstampDepositTransactionText());
		Matcher matcher = pattern.matcher(inputFile);
		int count = 0;
		while(matcher.find()) {
			++count;
		}
		
		return count;
	}

	private BigDecimal parseBalance(String inputFile) {
		String lastBalanceLine = null;
		Pattern pattern = Pattern.compile("(?m)^:62F:(.*)$");
		Matcher matcher = pattern.matcher(inputFile);
		while(matcher.find()) {
			lastBalanceLine = matcher.group(1);
		}
		
		int idx = lastBalanceLine.lastIndexOf("USD") + 3; /*length of USD*/
		String amountStr = lastBalanceLine.substring(idx);
		String amountStrWithDotAsDeliminer = amountStr.replace(',', '.');
		BigDecimal amount = new BigDecimal(amountStrWithDotAsDeliminer);
		return amount;
	}
	
}
