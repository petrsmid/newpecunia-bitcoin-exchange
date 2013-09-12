package com.newpecunia.configuration;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

import com.google.inject.Inject;
import com.newpecunia.NPException;

public class NPCredentials {

	private NPConfiguration configuration;
	private Configuration credentials = null;

	@Inject
	public NPCredentials(NPConfiguration configuration) {
		this.configuration = configuration;
		reloadCredentials();
	}
	
	public void reloadCredentials() {
		try {
			credentials = new XMLConfiguration(configuration.getCredentialsPath());
		} catch (ConfigurationException e) {
			throw new NPException("Cannot load file with credentials.", e);
		}		
	}	
	
	public String getBitstampUsername() {
		return credentials.getString("bitstamp.username");
	}

	public String getBitstampPassword() {
		return credentials.getString("bitstamp.password");
	}
	
		
}
