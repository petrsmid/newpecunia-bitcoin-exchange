package com.newpecunia.bitcoind.service.impl;

import java.math.BigDecimal;
import java.util.concurrent.locks.Lock;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import ru.paradoxs.bitcoin.client.BitcoinClient;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import com.newpecunia.bitcoind.service.BitcoindException;
import com.newpecunia.bitcoind.service.BitcoindService;
import com.newpecunia.bitcoind.service.TransactionInfo;
import com.newpecunia.bitcoind.service.TransactionInfo.Category;
import com.newpecunia.configuration.NPConfiguration;
import com.newpecunia.configuration.NPCredentials;
import com.newpecunia.persistence.entities.ReceivingBitcoinAddressStatus;
import com.newpecunia.persistence.entities.ReceivingBitcoinAddressStatus.AddressStatus;
import com.newpecunia.synchronization.LockProvider;

@Singleton
public class BitcoindServiceImpl implements BitcoindService {

	private static final Logger logger = LogManager.getLogger(BitcoindServiceImpl.class);	

	private NPCredentials credentials;
	private Provider<EntityManager> emProvider;
	private BitcoinClient btcClient = null;
	
	private Lock bitcoindLock;
	private Lock acquireAddressLock;
	
	@Inject
	BitcoindServiceImpl(Provider<EntityManager> emProvider, NPConfiguration configuration, NPCredentials credentials, LockProvider lockProvider) {
		this.emProvider = emProvider;
		this.credentials = credentials;		
		bitcoindLock = lockProvider.getLock();
		acquireAddressLock = lockProvider.getLock();
		
		btcClient = new BitcoinClient(configuration.getBitcoindServerWalletAddress(), credentials.getBitcoindRpcUser(), credentials.getBitcoindRpcPassword(), configuration.getBitcoindServerPort());
	}

	@Override
	public BigDecimal getBalance() {
		logger.trace("Getting balance of wallet.");
		bitcoindLock.lock();
		try {
			return btcClient.getBalance();
		} finally {
			bitcoindLock.unlock();
		}
	}

	@Override
	public void sendMoney(String address, BigDecimal amount, String comment, String commentTo) {
		logger.trace(String.format("Sending %s BTC to %s", amount.toPlainString(), address));
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
		logger.trace("Getting transaction info for tx: "+txId);
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

	@Override
	@Transactional
	public String acquireAddressForReceivingBTC() {
		logger.trace("Acquiring address for receiving BTC.");
		ReceivingBitcoinAddressStatus acquiredAddress = null;

		EntityManager em = getEntityManager();
		
		Session session = em.unwrap(Session.class);
		try {
			acquireAddressLock.lock(); //The lock is probably unneeded because we use PESSIMISTIC_WRITE (SELECT ... FOR UPDATE). 
			                           //However I am not sure whether some deadlock theoretically could not happen 
                                       // - because of changing selected rows. 
			acquiredAddress = (ReceivingBitcoinAddressStatus)
				session.createCriteria(ReceivingBitcoinAddressStatus.class)
				.add(Restrictions.eq("status", AddressStatus.FREE))
				.setMaxResults(1)
				.setLockMode(LockMode.PESSIMISTIC_WRITE)
				.uniqueResult();
		} finally {
			acquireAddressLock.unlock();
		}
		
		if (acquiredAddress == null) {
			throw new BitcoindException("All bitcoin addresses are in use!");
		}
		
		acquiredAddress.setStatus(AddressStatus.USED);
			
		return acquiredAddress.getAddress();
	}

	@Override
	@Transactional
	public void releaseAddressForReceivingBTC(String address) {
		logger.trace(String.format("Releasing address %s for receiving BTC.", address));
		EntityManager em = getEntityManager();
		
		Session session = em.unwrap(Session.class);
		ReceivingBitcoinAddressStatus addressToRelease = (ReceivingBitcoinAddressStatus)
			session.createCriteria(ReceivingBitcoinAddressStatus.class)
			.add(Restrictions.eq("address", address))
			.setMaxResults(1)
			.uniqueResult();
		
		if (addressToRelease == null) {
			logger.error("Bitcoin address "+address+" not found!");
		}	
		
		addressToRelease.setStatus(AddressStatus.FREE);
		
	}
	
	private EntityManager getEntityManager() {
		return emProvider.get();
	}	

}
