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

	private static final Logger logger = LogManager.getLogger(EmailSender.class);	
	
	
	private NPConfiguration configuration;
	private NPCredentials credentials;

	@Inject
	EmailSenderImpl(NPConfiguration configuration, NPCredentials credentials) {
		this.configuration = configuration;
		this.credentials = credentials;
	}
	
	@Override
	public void sendEmail(String recipientAddress, String subject, String messageBody) {
		final String username = configuration.getGmailAddress();
		final String password = credentials.getEmailPassword();
 
		Properties props = new Properties();
		//we use GMail. If you need another SMTP provider you must reimplement this part
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
 
		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});
 
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(configuration.getGmailAddress()));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientAddress));
			message.setSubject(subject);
			message.setText(messageBody);
			Transport.send(message);
		} catch (MessagingException e) {
			logger.error(String.format("Could not send e-mail to address %s.\nThe message: %s\n%s", recipientAddress, subject, messageBody), e);
		}
	}
}
