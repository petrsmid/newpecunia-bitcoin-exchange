package com.newpecunia.email;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.newpecunia.configuration.NPConfiguration;
import com.newpecunia.configuration.NPCredentials;

@Singleton
public class EmailSenderImpl implements EmailSender {

	private static final Logger logger = LogManager.getLogger(EmailSenderImpl.class);	
	
	
	private NPConfiguration configuration;
	private NPCredentials credentials;

	@Inject
	EmailSenderImpl(NPConfiguration configuration, NPCredentials credentials) {
		this.configuration = configuration;
		this.credentials = credentials;
	}

	@Override
	public void sendHtmlEmail(String recipientAddressList, String subject, String messageBody) {
		sendEmail(recipientAddressList, subject, messageBody, true);
	}
	
	@Override
	public void sendEmail(String recipientAddressList, String subject, String messageBody) {
		sendEmail(recipientAddressList, subject, messageBody, false);
	}
	
	private void sendEmail(String recipientAddressList, String subject, String messageBody, boolean asHtml) {
		logger.info("Sending e-mail "+recipientAddressList+" with subject "+subject);
		//TODO send backup email in BCC to our inbox
		final String username = configuration.getEmailAddress();
		final String password = credentials.getEmailPassword();
 
		Properties props = new Properties();
		props.put("mail.smtp.auth", "false");
		props.put("mail.smtp.starttls.enable", "false");
		props.put("mail.smtp.host", "localhost");
		props.put("mail.smtp.port", "25");
//        props.put("mail.debug", "true");

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});
 
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(configuration.getEmailAddress()));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientAddressList));
			message.setSubject(subject);
			message.setText(messageBody);
			if (asHtml) {
				message.setContent(messageBody, "text/html");    
			}
			Transport.send(message);
		} catch (MessagingException e) {
			logger.error(String.format("Could not send e-mail to address %s.\nThe message: %s\n%s", recipientAddressList, subject, messageBody), e);
		}
	}
}
