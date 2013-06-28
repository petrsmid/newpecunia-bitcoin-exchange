package com.petrsmid.bitexchange.bitstamp.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class SecureBitstampCredentialsImpl implements BitstampCredentials {

	private String username;
	private String password;
	
	private SecureBitstampCredentialsImpl() {}
	
	public static SecureBitstampCredentialsImpl newInstance() {
		SecureBitstampCredentialsImpl instance = new SecureBitstampCredentialsImpl();

		//TODO load credentials from secure storage
		// this code is just a stupid replacement for testing purposes
		Properties props = new Properties();
		try {
			props.load(new FileInputStream("/home/petr/bitstampcredentials.txt"));
		} catch (IOException e) {
			throw new RuntimeException("Could not load properties file with credentials.", e);
		}
		instance.username = props.getProperty("username");
		instance.password = props.getProperty("password");
		
		//END OF TODO
		
		return instance;		
	}
	
	@Override
	public String getUsername() {
		return username;
	}
	
	@Override
	public String getPassword() {
		return password;
	}

}
