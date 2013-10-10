package com.newpecunia.bitcoind.service.impl;

import java.math.BigDecimal;
import java.util.concurrent.locks.Lock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.paradoxs.bitcoin.client.BitcoinClient;

import com.google.inject.Inject;
import com.newpecunia.bitcoind.service.BitcoindException;
import com.newpecunia.bitcoind.service.BitcoindService;
import com.newpecunia.bitcoind.service.TransactionInfo;
import com.newpecunia.bitcoind.service.TransactionInfo.Category;
import com.newpecunia.configuration.NPConfiguration;
import com.newpecunia.configuration.NPCredentials;
import com.newpecunia.synchronization.LockProvider;

public class BitcoindServiceImpl implements BitcoindService {

	private static final Logger logger = LogManager.getLogger(BitcoindServiceImpl.class);	

	private NPCredentials credentials;
	private BitcoinClient btcClient = null;
	private Lock bitcoindLock = null;
	
	@Inject
	public BitcoindServiceImpl(NPConfiguration configuration, NPCredentials credentials, LockProvider lockProvider) {
		this.credentials = credentials;
		
		bitcoindLock = lockProvider.getLock();
		
		btcClient = new BitcoinClient(configuration.getBitcoindServerAddress(), credentials.getBitcoindRpcUser(), credentials.getBitcoindRpcPassword(), configuration.getBitcoindServerPort());
	}

	@Override
	public BigDecimal getBalance() {
		bitcoindLock.lock();
		try {
			return btcClient.getBalance();
		} finally {
			bitcoindLock.unlock();
		}
	}

	@Override
	public void sendMoney(String address, BigDecimal amount, String comment, String commentTo) {
		bitcoindLock.lock();
		try {
			btcClient.walletpassphrase(credentials.getBitcoindWalletPassword(), 10);   //unlock wallet
	        String txId = btcClient.sendToAddress(address, amount, comment, commentTo);
	        btcClient.walletlock(); //lock wallet
	        if (txId == null || txId.length() < 32) {
	        	throw new BitcoindException("Error ocurred while sending Bitcoins to "+address+". Amount: "+amount == null ? "null" : amount.toPlainString());
	        }
		} finally {
			bitcoindLock.unlock();
		}
	}
	
	@Override
	public TransactionInfo getTransactionInfo(String txId) {
		bitcoindLock.lock();
		try {
			return mapTransactionInfo(btcClient.getTransactionJSON(txId));
		} finally {
			bitcoindLock.unlock();
		}

	}

	private TransactionInfo mapTransactionInfo(ru.paradoxs.bitcoin.client.TransactionInfo paradoxsTx) {
		TransactionInfo tx = new TransactionInfo();
		tx.setAddress(paradoxsTx.getAddress());
		tx.setAmount(paradoxsTx.getAmount());
		for (Category category : Category.values()) {
			if (category.name().equalsIgnoreCase(paradoxsTx.getCategory())) {
				tx.setCategory(category);
			}
		}
		if (paradoxsTx.getCategory() != null && tx.getCategory() == null) {
			logger.error("Could not map bitcoin transaction category '"+paradoxsTx.getCategory()+"' to Category enum.");
		}
		tx.setComment(paradoxsTx.getComment());
		tx.setConfirmations(paradoxsTx.getConfirmations());
		tx.setFee(paradoxsTx.getFee());
		tx.setTxId(paradoxsTx.getTxId());
		return null;
	}

}
