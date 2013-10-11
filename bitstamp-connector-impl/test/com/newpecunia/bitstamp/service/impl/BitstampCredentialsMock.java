package com.newpecunia.bitstamp.service.impl;

import com.newpecunia.configuration.NPCredentials;

public class BitstampCredentialsMock extends NPCredentials {

	public BitstampCredentialsMock() {
		super(null);
	}

	@Override
	public String getBitstampUsername() {
		return "username";
	}

	@Override
	public String getBitstampPassword() {
		return "password";
	}
	
	@Override
	public void reloadCredentials() {
		//do nothing
	}

}
