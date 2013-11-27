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

	public String getBitcoindServerWalletAddress() {
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

	public BigDecimal getNbrOfBtcsForPriceCalculation() {
		return config.getBigDecimal("trader.nbrOfBtcsForPriceCalculation");
	}

	public BigDecimal getBitcoindTransactionFee() {
		return config.getBigDecimal("bitcoind.transactionFee");
	}
	
	public BigDecimal getOptimalBtcWalletBalance() {
		return config.getBigDecimal("trader.optimalBtcWalletBalance");
	}

	public BigDecimal getBitstampMinBtcReserve() {
		return config.getBigDecimal("trader.bitstampMinBtcReserve");
	}

	public BigDecimal getBitstampMinimalBtcOrder() {
		return config.getBigDecimal("bitstamp.minimalBtcOrder");
	}
	
	public BigDecimal getBitstampMinimalUsdWithdraw() {
		return config.getBigDecimal("bitstamp.minimalUsdWithdraw");
	}

	public long getAgeOfBtcOrderToReportInSec() {
		return config.getLong("trader.ageOfBtcOrderToReportInSec");
	}

	public String getBitstampWithdrawAccountCurrency() {
		return config.getString("bitstamp.withdrawAccountInfo.currency");
	}

	public String getBitstampWithdrawAccountName() {
		return config.getString("bitstamp.withdrawAccountInfo.name");
	}

	public String getBitstampWithdrawAccountAddress() {
		return config.getString("bitstamp.withdrawAccountInfo.address");
	}

	public String getBitstampWithdrawAccountCity() {
		return config.getString("bitstamp.withdrawAccountInfo.city");
	}

	public String getBitstampWithdrawAccountPostalCode() {
		return config.getString("bitstamp.withdrawAccountInfo.postalCode");
	}

	public String getBitstampWithdrawAccountCountry() {
		return config.getString("bitstamp.withdrawAccountInfo.country");
	}

	public String getBitstampWithdrawAccountBankName() {
		return config.getString("bitstamp.withdrawAccountInfo.bankName");
	}

	public String getBitstampWithdrawAccountBankAddress() {
		return config.getString("bitstamp.withdrawAccountInfo.bankAddress");
	}

	public String getBitstampWithdrawAccountBankCity() {
		return config.getString("bitstamp.withdrawAccountInfo.bankCity");
	}

	public String getBitstampWithdrawAccountBankPostalCode() {
		return config.getString("bitstamp.withdrawAccountInfo.bankPostalCode");
	}

	public String getBitstampWithdrawAccountBankCountry() {
		return config.getString("bitstamp.withdrawAccountInfo.bankCountry");
	}

	public String getBitstampWithdrawAccountIban() {
		return config.getString("bitstamp.withdrawAccountInfo.iban");
	}

	public String getBitstampWithdrawAccountBic() {
		return config.getString("bitstamp.withdrawAccountInfo.bic");
	}

	public String getBitstampWithdrawAccountComment() {
		return config.getString("bitstamp.withdrawAccountInfo.comment");
	}
	
	public String getBitstampBtcAddress() {
		return config.getString("bitstamp.btcAddress");
	}
	
	public String getBitstampDepositName() {
		return config.getString("bitstamp.deposit.name");
	}

	public String getBitstampDepositSurname() {
		return config.getString("bitstamp.deposit.surname");
	}

	public String getBitstampDepositAccountCurrency() {
		return config.getString("bitstamp.deposit.accountInfo.currency");
	}

	public String getBitstampDepositAccountName() {
		return config.getString("bitstamp.deposit.accountInfo.name");
	}

	public String getBitstampDepositAddress() {
		return config.getString("bitstamp.deposit.accountInfo.address");
	}

	public String getBitstampDepositCity() {
		return config.getString("bitstamp.deposit.accountInfo.city");
	}

	public String getBitstampDepositPostalCode() {
		return config.getString("bitstamp.deposit.accountInfo.postalCode");
	}

	public String getBitstampDepositCountry() {
		return config.getString("bitstamp.deposit.accountInfo.country");
	}

	public String getBitstampDepositBankIban() {
		return config.getString("bitstamp.deposit.accountInfo.bankIban");
	}

	public String getBitstampDepositBankSwift() {
		return config.getString("bitstamp.deposit.accountInfo.bankSwift");
	}

	public String getBitstampDepositBankName() {
		return config.getString("bitstamp.deposit.accountInfo.bankName");
	}

	public String getBitstampDepositBankAddress() {
		return config.getString("bitstamp.deposit.accountInfo.bankAddress");
	}

	public String getBitstampDepositBankCity() {
		return config.getString("bitstamp.deposit.accountInfo.bankCity");
	}

	public String getBitstampDepositBankPostalCode() {
		return config.getString("bitstamp.deposit.accountInfo.bankPostalCode");
	}

	public String getBitstampDepositBankCountry() {
		return config.getString("bitstamp.deposit.accountInfo.bankCountry");
	}
	
	public String getBitstampDepositTransactionText() {
		return config.getString("bitstamp.deposit.accountInfo.transactionText");
	}
	
	
}
