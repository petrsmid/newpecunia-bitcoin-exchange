package com.newpecunia.bitcoind.service.impl;

import java.math.BigDecimal;
import java.util.concurrent.locks.Lock;

import ru.paradoxs.bitcoin.client.BitcoinClient;

import com.google.inject.Inject;
import com.newpecunia.bitcoind.service.BitcoindException;
import com.newpecunia.bitcoind.service.BitcoindService;
import com.newpecunia.bitcoind.service.ReceiveMoneyCallback;
import com.newpecunia.configuration.NPConfiguration;
import com.newpecunia.configuration.NPCredentials;
import com.newpecunia.synchronization.LockProvider;

public class BitcoindServiceImpl implements BitcoindService {

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
		try {
			bitcoindLock.lock();
			return btcClient.getBalance();
		} finally {
			bitcoindLock.unlock();
		}
	}

	@Override
	public void sendMoney(String address, BigDecimal amount, String comment, String commentTo) {
		try {
			bitcoindLock.lock();
			
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
	public String addReceiveMoneyCallback(ReceiveMoneyCallback callback) {
		//TODO
		return null;
	}
	
	@Override
	public void removeReceiveMoneyCallback(String destinationAddress) {
		//TODO
	}

	

}
