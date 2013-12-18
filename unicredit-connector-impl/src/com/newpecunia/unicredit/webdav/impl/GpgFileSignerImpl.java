package com.newpecunia.unicredit.webdav.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.Security;
import java.security.SignatureException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPException;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.newpecunia.NPException;
import com.newpecunia.configuration.NPCredentials;
import com.newpecunia.time.TimeProvider;
import com.newpecunia.unicredit.webdav.impl.pgp.BouncyCastleSigner;

@Singleton
public class GpgFileSignerImpl implements GpgFileSigner {
	
	private static final Logger logger = LogManager.getLogger(GpgFileSignerImpl.class);	
	
	private NPCredentials credentials;
	private Provider securityProvider;
	private TimeProvider timeProvider;

	@Inject
	GpgFileSignerImpl(NPCredentials credentials, TimeProvider timeProvider) {
		this.timeProvider = timeProvider;
		this.credentials = credentials;
		this.securityProvider = new BouncyCastleProvider();
        Security.addProvider(securityProvider);
	}
	
	@Override
	public byte[] sign(byte[] content) {
		logger.trace("Signing package file");
        try {
			return BouncyCastleSigner.signFile(content, "fakeFileName", new FileInputStream(credentials.getPrivateSignatureKeyFilePath()), 
					credentials.getPrivateKeyPassword().toCharArray(), securityProvider, timeProvider);
		} catch (NoSuchAlgorithmException | NoSuchProviderException
				| SignatureException | IOException
				| PGPException e) {
			
			throw new NPException("Error ocurred while signing the file.", e);
		}
	}

	
	
}
