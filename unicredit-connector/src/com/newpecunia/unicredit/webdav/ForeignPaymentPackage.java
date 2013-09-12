package com.newpecunia.unicredit.webdav;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import com.newpecunia.ProgrammerException;
import com.newpecunia.time.TimeProvider;
import com.newpecunia.unicredit.service.ForeignPayment;

public class ForeignPaymentPackage {

	private static final String NEWLINE = "\r\n";

	private ForeignPayment payment;

	private TimeProvider timeProvider;

	private String reference;
	
	/**
	 * We use "one payment per package" strategy.
	 */
	public ForeignPaymentPackage(String reference, ForeignPayment foreignPayment, TimeProvider timeProvider) {
		this.reference = reference;
		this.payment = foreignPayment;
		this.timeProvider = timeProvider;
	}
	
	
	public String toMultiCash() {
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
		appendVariable(builder, 11, formatField("UNCRSKBX")); //TODO make configurable
		appendNewLine(builder);

		appendFixed(builder, 4, ":05:");
		appendVariable(builder, 35, formatField("FIRMA s.r.o")); //TODO make configurable
		appendNewLine(builder);
		appendVariable(builder, 35, formatField("Ulice 123")); //TODO make configurable
		appendNewLine(builder);
		appendVariable(builder, 35, formatField("Mesto")); //TODO make configurable
		appendNewLine(builder);
		appendVariable(builder, 35, formatField("Czech republic")); //TODO make configurable
		appendNewLine(builder);
		
		String payerBankSwift = formatField("UNCRSKBXAXXX"); //TODO make configurable
		String payeeSwift = formatField(payment.getSwift()); //TODO FIXED or VARIABLE size?
		builder.append("{1:F01"+payerBankSwift+"0001000001}{2:I100"+payeeSwift+"N1}{4:");
		
		//body (text block)
		appendFixed(builder, 4, ":20:");
		appendFixed(builder, 16, reference);
		appendNewLine(builder);

		appendFixed(builder, 5, ":32A:");
		appendFixed(builder, 6, timeProvider.nowDateTime().toString("yyMMdd"));
		appendFixed(builder, 3, formatField(payment.getCurrency()));
		appendNewLine(builder);

		appendFixed(builder, 4, ":50:");
		appendVariable(builder, 35, formatField("FIRMA s.r.o")); //TODO make configurable
		appendNewLine(builder);
		appendVariable(builder, 35, formatField("Ulice 123")); //TODO make configurable
		appendNewLine(builder);
		appendVariable(builder, 35, formatField("Mesto")); //TODO make configurable
		appendNewLine(builder);
		appendVariable(builder, 35, formatField("Czech republic")); //TODO make configurable
		appendNewLine(builder);
		
		appendFixed(builder, 5, ":52D:");
		appendFixed(builder, 16, formatField("0000001234567890123456"));  //TODO make configurable. Btw mistake in example??? Btw. must be leading zeros?
		appendNewLine(builder);
		appendFixed(builder, 16, formatField("0000001234567890123456"));  //TODO make configurable. Btw mistake in example??? Btw. must be leading zeros?
		appendNewLine(builder);
		appendFixed(builder, 3, formatField("USD")); //TODO make configurable - account currency
		appendBlank(builder);
		appendFixed(builder, 3, formatField("USD")); //TODO make configurable - charge account currency
		appendNewLine(builder);
		appendFixed(builder, 3, formatField("123")); //TODO make configurable - Btw. what is this? - statistical code
		appendBlank(builder);
		appendFixed(builder, 3, formatField(payment.getCountry().getIsoCode()));
		appendBlank(builder);
		appendFixed(builder, 3, formatField(payment.getBankCountry().getIsoCode()));
		appendNewLine(builder);

		appendFixed(builder, 5, ":57A:");
		appendVariable(builder, 11, formatField(payment.getSwift())); //TODO how come only 11 chars?
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
		appendVariable(builder, 35, formatField(payment.getName()));
		appendNewLine(builder);
		appendVariable(builder, 35, formatField(payment.getAddress() + " " + payment.getCity())); //TODO be more intelligent - if street + city is longer than field make some optimizations
		appendNewLine(builder);
		
		appendFixed(builder, 4, ":70:");
		appendVariable(builder, 35, formatField("Payment detail 1")); //TODO make configurable
		appendVariable(builder, 35, formatField("Payment detail 2")); //TODO make configurable
		appendVariable(builder, 35, formatField("Payment detail 3")); //TODO make configurable
		appendVariable(builder, 35, formatField("Payment detail 4")); //TODO make configurable
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
		
		appendFixed(builder, 35, "Petr Smid, telephone"); //TODO make configurable
		appendNewLine(builder);
		appendFixed(builder, 35, "extended text message"); //TODO make configurable
		appendNewLine(builder);
		
		//footer
		builder.append("-}");
		
		return builder.toString();
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
