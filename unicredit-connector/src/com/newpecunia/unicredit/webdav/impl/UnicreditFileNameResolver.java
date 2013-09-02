package com.newpecunia.unicredit.webdav.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import com.newpecunia.persistence.IdGenerator;

public class UnicreditFileNameResolver {

	private static final int ID_LENGTH = IdGenerator.INSTANCE.nextId().length();

	private static final String SUFFIX = ".csv";

	private static final Logger logger = LogManager.getLogger(UnicreditFileNameResolver.class);	
	
	public String getUploadFileNameForId(String id) {
		DateTime dt = new DateTime();
		String timeString = dt.toString("yyyy-MM-dd_HH-mm-ss");
		return timeString + "_" + id + SUFFIX;
	}
	
	public String createNewUploadFileName() {
		return getUploadFileNameForId(IdGenerator.INSTANCE.nextId());
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
