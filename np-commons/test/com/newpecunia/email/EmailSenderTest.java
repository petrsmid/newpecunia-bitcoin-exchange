package com.newpecunia.email;

import org.junit.Test;

import com.newpecunia.configuration.NPConfiguration;
import com.newpecunia.configuration.NPCredentials;

public class EmailSenderTest {
	
	@Test
	public void testSendingEmail() {
		NPConfiguration configuration = new NPConfiguration();
		EmailSender es = new EmailSenderImpl(configuration, new NPCredentials(configuration));
		
		es.sendEmail("petr.smid@pekat.cz", "Test message", "Test body\nline1\nline2\n\nBest regards");
	}

}
