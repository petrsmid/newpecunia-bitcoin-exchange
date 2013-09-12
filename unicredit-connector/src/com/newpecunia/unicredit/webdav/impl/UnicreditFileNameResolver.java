package com.newpecunia.unicredit.webdav.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

public class UnicreditFileNameResolver {

	private static final String SUFFIX = ".csv";

	private static final Logger logger = LogManager.getLogger(UnicreditFileNameResolver.class);	
	
	public String getUploadFileNameForId(String id) {
		DateTime dt = new DateTime();
		String timeString = dt.toString("yyyy-MM-dd_HH-mm-ss");
		return timeString + "_" + id + SUFFIX;
	}
	
	public String getIdFromStatusFile(String fileName) {
		try {
			int idx1 = fileName.lastIndexOf('_');
			int idx2 = fileName.indexOf('.', idx1);
			if (idx1 < 0 || idx2 < 0) {
				throw new RuntimeException("File name '"+fileName+"' does not contain '_' or '.'");
			}
			String id = fileName.substring(idx1+1, idx2);
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
			if (idx1 < 0 || idx2 < 0) {
				throw new RuntimeException("File name '"+fileName+"' does not contain '_' or '.'");
			}
			String id = fileName.substring(idx1+1, idx2);
			return id;
		} catch (Exception e) {
			logger.warn("Could not parse file name '"+fileName+"' (in upload folder).");
			return null;
		}
	}

}
