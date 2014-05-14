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
	
	public BigDecimal getInterestInPercent() {
		return config.getBigDecimal("interestInPercent");
	}
	
	public String getBitcoindServerWalletAddress() {
		return config.getString("bitcoind.address");
	}

	public int getBitcoindServerPort() {
		return config.getInt("bitcoind.port");
	}

	public BigDecimal getBitcoindTransactionFee() {
		return config.getBigDecimal("bitcoind.transactionFee");
	}
	
	public String getEmailAddress() {
		return config.getString("email.address");
	}
	
	public long getBtcPriceActualisationPeriodInSecs() {
		return config.getLong("trader.btcPriceActualisationPeriodInSecs");
	}

	public BigDecimal getNbrOfBtcsForPriceCalculation() {
		return config.getBigDecimal("trader.nbrOfBtcsForPriceCalculation");
	}

	public BigDecimal getOptimalBtcWalletBalance() {
		return config.getBigDecimal("trader.optimalBtcWalletBalance");
	}

	public long getAgeOfBtcOrderToReportInSec() {
		return config.getLong("trader.ageOfBtcOrderToReportInSec");
	}

	public BigDecimal getBitstampMinimalBtcOrder() {
		return config.getBigDecimal("bitstamp.minimalBtcOrder");
	}
	
	public String getBitstampBtcAddress() {
		return config.getString("bitstamp.btcAddress");
	}
	
	public BigDecimal getBitstampWithdrawUsdFeePercent() {
		return config.getBigDecimal("bitstamp.withdrawUsdFeePercent");
	}

	public BigDecimal getBitstampMinWithdrawUsdFee() {
		return config.getBigDecimal("bitstamp.minWithdrawUsdFee");
	}

	public long getMaxOrderbookAgeInMsToBeConsideredActual() {
		return config.getLong("bitstamp.maxOrderbookAgeInMsToBeConsideredActual");
	}
	
	
	
	public String getCardProcessingUrl() {
		return config.getString("cardPayment.processingUrl");
	}

	
	

}
