package com.newpecunia.unicredit.webdav.impl;

import java.nio.ByteBuffer;
import java.util.Random;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

public class UnicreditFileNameResolver {

	private static final int ID_LENGTH = 32;

	private static final String SUFFIX = ".csv";

	private static final Logger logger = LogManager.getLogger(UnicreditFileNameResolver.class);	
	
	private Random random = new Random();
	
	/**
	 * Constructs ID based on UUID for time and random number 
	 */
	public String generateNewId() {
		long now = System.nanoTime();
		byte[] timeAsBytes = ByteBuffer.allocate(8).putLong(now).array();
		
        byte[] randomAsBytes = new byte[8];
		random.nextBytes(randomAsBytes);
		
		//concatenate byte arrays
		byte[] bytesForUuid3 = new byte[timeAsBytes.length + randomAsBytes.length];
		System.arraycopy(timeAsBytes, 0, bytesForUuid3, 0, timeAsBytes.length);
		System.arraycopy(randomAsBytes, 0, bytesForUuid3, timeAsBytes.length, randomAsBytes.length);
		
		//construct UUID based on the timestamp and random
		UUID uuid = UUID.nameUUIDFromBytes(bytesForUuid3);
		return uuid.toString().replaceAll("-", "");
	}
	
	
	public String getUploadFileNameForId(String id) {
		DateTime dt = new DateTime();
		String timeString = dt.toString("yyyy-MM-dd_HH-mm-ss");
		return timeString + "_" + id + SUFFIX;
	}
	
	public String createNewUploadFileName() {
		return getUploadFileNameForId(generateNewId());
	}
	
	public String getIdFromStatusFile(String fileName) {
		try {
			int idx1 = fileName.lastIndexOf('_');
			int idx2 = fileName.indexOf('.', idx1);
			String id = fileName.substring(idx1+1, idx2);
			if (id.length() != ID_LENGTH) {
				logger.warn("Parsed ID ('"+id+"')of file '"+fileName+"' from status folder does not have "+ID_LENGTH+" characters. Ignoring!");
				return null;
			}
			return id;
		} catch (Exception e) {
			logger.warn("Could not parse file name '"+fileName+"' (in status folder).");
			return null;
		}
	}
	
	public String getIdFromUploadFile(String fileName) {
		try {
			int idx1 = fileName.lastIndexOf('_');
			int idx2 = fileName.indexOf('.', idx1);
			String id = fileName.substring(idx1+1, idx2);
			if (id.length() != ID_LENGTH) {
				logger.warn("Parsed ID ('"+id+"')of file '"+fileName+"' from upload folder does not have "+ID_LENGTH+" characters. Ignoring!");
				return null;
			}
			return id;
		} catch (Exception e) {
			logger.warn("Could not parse file name '"+fileName+"' (in upload folder).");
			return null;
		}
	}

}
