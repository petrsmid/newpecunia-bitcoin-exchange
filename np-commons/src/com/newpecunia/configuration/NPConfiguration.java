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
	
	public String getWebdavBaseFolder() {
		return config.getString("unicredit.webdav.baseurl");
	}

	public String getWebdavForeignUploadFolder() {
		return config.getString("unicredit.webdav.upload.foreignfolder");
	}

	public String getWebdavStatusFolder() {
		return config.getString("unicredit.webdav.statusfolder");
	}
	
	public String getWebdavStatementsFolder() {
		return config.getString("unicredit.webdav.statements.folder");
	}

	public String getWebdavStatementsFileName() {
		return config.getString("unicredit.webdav.statements.filename");
	}
	
	public BigDecimal getUnicreditReserve() {
		return config.getBigDecimal("unicredit.accountReserve");
	}

	public String getPayerAccountCurrency() {
		return config.getString("unicredit.payerAccountCurrency");
	}
	
	public String getPayerAccountNumber() {
		return config.getString("unicredit.payerAccountNumber");
	}
	
	public String getPayerName() {
		return config.getString("unicredit.payerName");
	}	

	public String getPayerStreet() {
		return config.getString("unicredit.payerStreet");
	}	
	
	public String getPayerCity() {
		return config.getString("unicredit.payerCity");
	}	

	public String getPayerCountry() {
		return config.getString("unicredit.payerCountry");
	}	

	public String getPayerBankSwift() {
		return config.getString("unicredit.payerBankSwift");
	}	

	public String getPayerBankSwiftLong() {
		return config.getString("unicredit.payerBankSwiftLong");
	}	

	public String getPaymentStatisticalCode() {
		return config.getString("unicredit.paymentStatisticalCode");
	}	

	public BigDecimal getPaymentFee() {
		return config.getBigDecimal("unicredit.paymentFee");
	}

	public String getBitcoindServerAddress() {
		return config.getString("bitcoind.address");
	}

	public int getBitcoindServerPort() {
		return config.getInt("bitcoind.port");
	}

	public String getGmailAddress() {
		return config.getString("gmail.address");
	}
	
	public String getReportingEmailAddress() {
		return config.getString("reportingEmail");
	}

	public int getMaxTimeToWaitForBtcInSeconds() {
		return config.getInt("maxTimeToWaitForBtcInSeconds");
	}

	public long getBtcPriceActualisationPeriodInSecs() {
		return config.getLong("trader.btcPriceActualisationPeriodInSecs");
	}

	public int getNbrOfBtcsForPriceCalculation() {
		return config.getInt("trader.nbrOfBtcsForPriceCalculation");
	}

}
