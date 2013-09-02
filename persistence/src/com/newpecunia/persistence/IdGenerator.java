package com.newpecunia.persistence;

import java.nio.ByteBuffer;
import java.util.Random;

import org.apache.commons.codec.binary.Base32;

public enum IdGenerator {

	INSTANCE;
	
	private Random randomizer = new Random(); //it is thread safe
	private static final char GENERATOR_VERSION = '1';
	
	public String nextId() {		
		long miliTime = System.currentTimeMillis();
		long randomValue = randomizer.nextLong();
		ByteBuffer byteBuffer = ByteBuffer.allocate(16);		
		byteBuffer.putLong(miliTime);
		byteBuffer.putLong(randomValue);
		
		Base32 base32 = new Base32(); //we do not use base64 because it uses characters '/', '+' and '-' and we want only alphanumeric characters
		String base32Id = base32.encodeToString(byteBuffer.array());
		
		String newId = base32Id.replaceAll("=", "");
		return GENERATOR_VERSION + newId;		
	}

}
