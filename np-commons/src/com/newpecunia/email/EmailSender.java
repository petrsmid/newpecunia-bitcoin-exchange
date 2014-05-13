package com.newpecunia.email;


public interface EmailSender {

	void sendEmail(String recipientAddress, String subject, String messageBody);
	void sendHtmlEmail(String recipientAddress, String subject, String messageBody);

}
