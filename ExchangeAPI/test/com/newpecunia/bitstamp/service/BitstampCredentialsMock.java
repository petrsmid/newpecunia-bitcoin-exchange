package com.newpecunia.bitstamp.service;

import com.newpecunia.bitstamp.service.impl.BitstampCredentials;

public class BitstampCredentialsMock implements BitstampCredentials {

	@Override
	public String getUsername() {
		return "username";
	}

	@Override
	public String getPassword() {
		return "password";
	}

}
