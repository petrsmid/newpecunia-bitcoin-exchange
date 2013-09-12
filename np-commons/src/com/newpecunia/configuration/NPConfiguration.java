package com.newpecunia.configuration;

import java.math.BigDecimal;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

import com.newpecunia.NPException;

public class NPConfiguration {
	
	private static final String CONFIG_FILENAME = "npconfig.xml";
	
	public NPConfiguration() {
		reloadConfiguration();
	}
	
	private Configuration config = null;
	
	public void reloadConfiguration() {
		try {
			config = new XMLConfiguration(getClass().getResource(CONFIG_FILENAME));
		} catch (ConfigurationException e) {
			throw new NPException("Cannot load config file.", e);
		}		
	}

	public String getCredentialsPath() {
		return config.getString("credentialsPath");
	}
	
	public String getWebdavEncoding() {
		return config.getString("unicredit.webdav.fileencoding");
	}

	public String getWebdavBaseFolder() {
		return config.getString("unicredit.webdav.baseurl");
	}

	public String getWebdavForeignUploadFolder() {
		return config.getString("unicredit.webdav.upload.foreignfolder");
	}

	public String getWebdavStatusFolder() {
		return config.getString("unicredit.webdav.statusfolder");
	}

	/**
	 * update balance from Unicredit maximaly every 10 minutes
	 */
	public long getBalanceUpdatePeriad() {
		return config.getLong("unicredit.balance_update_period_ms");
	}

	public String getUnicreditAccountCurrency() {
		return config.getString("unicredit.account_currency");
	}

	public BigDecimal getUnicreditReserve() {
		return config.getBigDecimal("unicredit.account_reserve");
	}

	public BigDecimal getPaymentFee() {
		return config.getBigDecimal("unicredit.payment_fee");
	}
	
	
}
