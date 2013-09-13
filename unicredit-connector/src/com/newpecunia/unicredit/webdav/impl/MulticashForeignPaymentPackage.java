package com.newpecunia.unicredit.webdav.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import com.newpecunia.ProgrammerException;
import com.newpecunia.configuration.NPConfiguration;
import com.newpecunia.time.TimeProvider;
import com.newpecunia.unicredit.service.ForeignPayment;

public class MulticashForeignPaymentPackage {

	private static final String NEWLINE = "\r\n";

	private ForeignPayment payment;

	private TimeProvider timeProvider;

	private String reference;

	private NPConfiguration configuration;
	
	/**
	 * We use "one payment per package" strategy.
	 */
	public MulticashForeignPaymentPackage(String reference, ForeignPayment foreignPayment, TimeProvider timeProvider, NPConfiguration configuration) {
		this.reference = reference;
		this.payment = foreignPayment;
		this.timeProvider = timeProvider;
		this.configuration = configuration;
	}
	
	
	/**
	 * Creates file content for the foreign multicash package file. Always use UTF-8 to encode the file. 
	 */
	public String toMultiCashFileContent() {
		BigDecimal roundedAmount = payment.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP);
		
		DecimalFormat formatter = new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(Locale.GERMAN)); //We don't want to use default locale. The German Locale is OK.
		String amountStr = formatter.format(roundedAmount); 					

		//header
		StringBuilder builder = new StringBuilder();
		appendNewLine(builder);
		appendFixed(builder, 4, ":01:");
		if (!StringUtils.isBlank(reference)) {
			appendVariable(builder, 16, reference);
		}
		appendNewLine(builder);
		
		appendFixed(builder, 4, ":02:");
		appendVariable(builder, 17, amountStr);
		appendNewLine(builder);

		appendFixed(builder, 4, ":03:");
		appendVariable(builder, 5, "1"); //number of payments is always 1
		appendNewLine(builder);
		
		appendFixed(builder, 4, ":04:");
		appendVariable(builder, 11, formatField(configuration.getPayerBankSwift()));
		appendNewLine(builder);

		appendFixed(builder, 4, ":05:");
		appendVariable(builder, 35, formatField(configuration.getPayerName()));
		appendNewLine(builder);
		if (!StringUtils.isBlank(configuration.getPayerStreet())) {
			appendVariable(builder, 35, formatField(configuration.getPayerStreet()));
			appendNewLine(builder);
		}
		if (!StringUtils.isBlank(configuration.getPayerCity())) {
			appendVariable(builder, 35, formatField(configuration.getPayerCity()));
			appendNewLine(builder);
		}
		if (!StringUtils.isBlank(configuration.getPayerCountry())) {
			appendVariable(builder, 35, formatField(configuration.getPayerCountry()));
			appendNewLine(builder);
		}
		appendFixed(builder, 4, ":07:");
		String referenceSuffix = reference.length() <= 12 ? reference : reference.substring(reference.length()-12, reference.length());
		appendVariable(builder, 12, referenceSuffix); //should be file name but it is not necessary to provide the real name of the file
		appendNewLine(builder);

		
		builder.append("{1:F01");
		appendVariable(builder, 16, formatField(configuration.getPayerBankSwiftLong()));
		builder.append("0001000001}{2:I100");
		appendVariable(builder, 16, formatField(payment.getSwift()));
		builder.append("N1}{4:");
		appendNewLine(builder);
		
		//body (text block)
		appendFixed(builder, 4, ":20:");
		appendFixed(builder, 16, reference);
		appendNewLine(builder);

		appendFixed(builder, 5, ":32A:");
		appendFixed(builder, 6, timeProvider.nowDateTime().toString("yyMMdd"));
		appendFixed(builder, 3, formatField(payment.getCurrency()));
		appendVariable(builder, 15, amountStr);
		appendNewLine(builder);

		appendFixed(builder, 4, ":50:");
		appendVariable(builder, 35, formatField(configuration.getPayerName()));
		appendNewLine(builder);
		if (!StringUtils.isBlank(configuration.getPayerStreet())) {
			appendVariable(builder, 35, formatField(configuration.getPayerStreet()));
			appendNewLine(builder);
		}
		if (!StringUtils.isBlank(configuration.getPayerCity())) {
			appendVariable(builder, 35, formatField(configuration.getPayerCity()));
			appendNewLine(builder);
		}
		if (!StringUtils.isBlank(configuration.getPayerCountry())) {
			appendVariable(builder, 35, formatField(configuration.getPayerCountry()));
			appendNewLine(builder);
		}
		
		appendFixed(builder, 5, ":52D:");
		String accountWithLeadingZeroes = addLeadingZeroes(formatField(configuration.getPayerAccountNumber()),16);
		appendFixed(builder, 16, formatField(accountWithLeadingZeroes));
		appendNewLine(builder);
		appendFixed(builder, 16, formatField(accountWithLeadingZeroes));
		appendNewLine(builder);
		appendFixed(builder, 3, formatField(configuration.getPayerAccountCurrency()));
		appendBlank(builder);
		appendFixed(builder, 3, formatField(configuration.getPayerAccountCurrency()));
		appendNewLine(builder);
		appendFixed(builder, 3, formatField(configuration.getPaymentStatisticalCode()));
		appendBlank(builder);
		appendFixed(builder, 2, formatField(payment.getCountry().getIsoCode()));
		appendBlank(builder);
		appendFixed(builder, 2, formatField(payment.getBankCountry().getIsoCode()));
		appendNewLine(builder);

		appendFixed(builder, 5, ":57A:");
		appendVariable(builder, 34, formatField(payment.getSwift()));
		appendNewLine(builder);

		appendFixed(builder, 5, ":57D:");
		appendVariable(builder, 35, formatField(payment.getBankName()));
		appendNewLine(builder);
		appendVariable(builder, 35, formatField(payment.getBankAddress()));
		appendNewLine(builder);
		appendVariable(builder, 35, formatField(payment.getBankCity()));
		appendNewLine(builder);
		
		String bankCountry = payment.getBankCountry().getEnglishName();
		if (bankCountry.length() > 35) {bankCountry = payment.getBankCountry().getIsoCode();}
		appendVariable(builder, 35, formatField(bankCountry));
		appendNewLine(builder);

		appendFixed(builder, 4, ":59:");
		builder.append('/');
		appendVariable(builder, 34, formatField(payment.getAccountNumber()));
		appendNewLine(builder);
		appendVariable(builder, 35, formatField(payment.getName()));
		appendNewLine(builder);
		if (!StringUtils.isBlank(payment.getAddress())) {
			appendVariable(builder, 35, formatField(payment.getAddress()));
			appendNewLine(builder);
		}
		if (!StringUtils.isBlank(payment.getCity()) || !StringUtils.isBlank(payment.getPostalCode())) {
			String city = payment.getCity() == null ? "" : payment.getCity();
			String postalCode = payment.getPostalCode() == null ? "" : payment.getPostalCode().replaceAll(" ", "");
			String pcWithCity = postalCode + " " + city;
			if (pcWithCity.length() > 35) {
				pcWithCity = city.substring(0, Math.min(35, city.length()));
			}
			appendVariable(builder, 35, formatField(pcWithCity));		
			appendNewLine(builder);
		}
		if (payment.getCountry() != null) {
			String countryName = payment.getCountry().getEnglishName();
			if (countryName.length() > 35) {countryName = payment.getCountry().getIsoCode();}
			appendVariable(builder, 35, countryName);		
			appendNewLine(builder);
		}
				
		appendFixed(builder, 4, ":70:");
		if (!StringUtils.isBlank(configuration.getPaymentDetailToCustomer())) {
			appendVariable(builder, 35, formatField(configuration.getPaymentDetailToCustomer())); //TODO add possibility to choose
			appendNewLine(builder);
			//here are allowed 4 lines but we use only the first one
		}
	
		appendFixed(builder, 5, ":71A:");
		appendFixed(builder, 3, "BN1"); //TODO add possibility to specify charges splitting details
		appendNewLine(builder);
		
		appendFixed(builder, 4, ":72:");
		appendFixed(builder, 2, "00");
		appendBlank(builder);
		appendFixed(builder, 2, "00");
		appendBlank(builder);
		appendFixed(builder, 2, "00");
		appendBlank(builder);
		appendFixed(builder, 2, "00");
		appendNewLine(builder);
		
		appendFixed(builder, 35, ""); //must be empty - filled with spaces
		appendNewLine(builder);
		appendFixed(builder, 35, ""); //must be empty - filled with spaces
		appendNewLine(builder);
		appendFixed(builder, 35, ""); //must be empty - filled with spaces
		appendNewLine(builder);
		
		//footer
		builder.append("-}");
		
		return builder.toString();
	}
			

	private String addLeadingZeroes(String text, int length) {
		StringBuilder sb = new StringBuilder(text);
		while (sb.length() < length) {
			sb.insert(0, '0');
		}
		return sb.toString();
	}


	private void appendFixed(StringBuilder builder, int length, String text) {
		appendVariable(builder, length, text);
		//add whitespace
		for(int i = text.length(); i < length; i++) {
			builder.append(' ');
		}
	}
	
	private void appendVariable(StringBuilder builder, int maxLength, String text) {
		 if (text.length() > maxLength) {
			 throw new ProgrammerException("Error ocurred while creating multicash package. " +
			 		"The text '"+text+"' is longer as "+maxLength+" characters.");
		 } else {
			 builder.append(text == null ? "" : formatTextToAllowedChars(text));
		 }
	}
	
	private String formatTextToAllowedChars(String text) {
		//Here it is possible to filter characters or transfer them to e.g. ASCII. 
		//However according to my tests the bank accepts UTF-8 without any problems.
		return text;
	}
	
	private String formatField(String text) {
		if (text == null) {
			return "";
		} else {
			return formatTextToAllowedChars(text.trim());
		}
	}
	
	private void appendNewLine(StringBuilder builder) {
		builder.append(NEWLINE);
	}

	private void appendBlank(StringBuilder builder) {
		builder.append(' ');
	}
}
