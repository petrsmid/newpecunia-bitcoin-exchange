package com.newpecunia.unicredit.webdav.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;

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
	
	
	public String toMultiCashString() {
		BigDecimal roundedAmount = payment.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP);
		DecimalFormat formatter = new DecimalFormat("0,00");
		String amountStr = formatter.format(roundedAmount); 					

		//header
		StringBuilder builder = new StringBuilder();
		appendFixed(builder, 4, ":01:");
		appendVariable(builder, 16, reference);
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
		appendVariable(builder, 35, formatField(configuration.getPayerStreet()));
		appendNewLine(builder);
		appendVariable(builder, 35, formatField(configuration.getPayerCity()));
		appendNewLine(builder);
		appendVariable(builder, 35, formatField(configuration.getPayerCountry()));
		appendNewLine(builder);
		
		builder.append("{1:F01");
		appendVariable(builder, 16, formatField(configuration.getPayerBankSwift()));
		builder.append("0001000001}{2:I100");
		appendVariable(builder, 16, formatField(payment.getSwift()));
		builder.append("N1}{4:");
		
		//body (text block)
		appendFixed(builder, 4, ":20:");
		appendFixed(builder, 16, reference);
		appendNewLine(builder);

		appendFixed(builder, 5, ":32A:");
		appendFixed(builder, 6, timeProvider.nowDateTime().toString("yyMMdd"));
		appendFixed(builder, 3, formatField(payment.getCurrency()));
		appendNewLine(builder);

		appendFixed(builder, 4, ":50:");
		appendVariable(builder, 35, formatField(configuration.getPayerName()));
		appendNewLine(builder);
		appendVariable(builder, 35, formatField(configuration.getPayerStreet()));
		appendNewLine(builder);
		appendVariable(builder, 35, formatField(configuration.getPayerCity()));
		appendNewLine(builder);
		appendVariable(builder, 35, formatField(configuration.getPayerCountry()));
		appendNewLine(builder);
		
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
		appendFixed(builder, 3, formatField(payment.getCountry().getIsoCode()));
		appendBlank(builder);
		appendFixed(builder, 3, formatField(payment.getBankCountry().getIsoCode()));
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

		appendFixed(builder, 4, ":59:");
		builder.append('/');
		appendVariable(builder, 34, formatField(payment.getAccountNumber()));
		appendNewLine(builder);
		String[] twoLinesAddress = optimizeAddressToTwoLines(35, 35, payment.getName(), payment.getAddress(), payment.getCity());
		appendVariable(builder, 35, formatField(twoLinesAddress[0]));
		appendNewLine(builder);
		appendVariable(builder, 35, formatField(twoLinesAddress[1]));
		appendNewLine(builder);
		
		appendFixed(builder, 4, ":70:");
		appendVariable(builder, 35, formatField(configuration.getPaymentDetailToCustomer())); //TODO add possibility to choose
//		appendVariable(builder, 35, formatField("Payment detail 2"));
//		appendVariable(builder, 35, formatField("Payment detail 3"));
//		appendVariable(builder, 35, formatField("Payment detail 4"));
		appendNewLine(builder);

		appendFixed(builder, 4, ":71:");
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
		
		appendFixed(builder, 35, formatField(configuration.getPaymentContact()));
		appendNewLine(builder);
		appendFixed(builder, 35, ""); //must be empty - filled with spaces
		appendNewLine(builder);
		
		//footer
		builder.append("-}");
		
		return builder.toString();
	}
			
	private String[] optimizeAddressToTwoLines(int line1Length, int line2Length, String name, String street, String city) {

		return null;
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
		String upperText = text.toUpperCase();
		char[] output = new char[upperText.length()];
		int i = 0;
		for (char ch : upperText.toCharArray()) {
			if ((ch >= '0' && ch <= '9')||(ch >= 'A' && ch <= 'Z')) {
				output[i]=ch;
			} else {
				output[i]=' '; //replace the invalid character with space
			}
			i++;
		}
		return new String(output);
	}
	
	private String formatField(String text) {
		return formatTextToAllowedChars(text.trim());
	}
	
	private void appendNewLine(StringBuilder builder) {
		builder.append(NEWLINE);
	}

	private void appendBlank(StringBuilder builder) {
		builder.append(' ');
	}
}
