package com.newpecunia.configuration;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import com.newpecunia.NPException;

public enum NPConfiguration {
	
	INSTANCE;

	private static final String CONFIG_FILENAME = "npconfig.properties";
	
	private Configuration config = null;
	
	private NPConfiguration() {
		reloadConfiguration();
	}
	
	public static void reloadConfiguration() {
		try {
			INSTANCE.config = new PropertiesConfiguration(CONFIG_FILENAME);
		} catch (ConfigurationException e) {
			throw new NPException("Cannot load config file.", e);
		}		
	}
	
	public String getWebdavEncoding() {
		return config.getString("webdav.fileencoding");
	}

	public String getWebdavBaseFolder() {
		return config.getString("webdav.baseurl");
	}

	public String getWebdavForeignUploadFolder() {
		return config.getString("webdav.upload.foreignfolder");
	}

	public String getWebdavStatusFolder() {
		return config.getString("webdav.statusfolder");
	}
}
