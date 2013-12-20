package com.newpecunia.unicredit.webdav.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.newpecunia.unicredit.webdav.Status;

public class StatusImpl implements Status {

	private static final Logger logger = LogManager.getLogger(StatusImpl.class);	
	
	private StatusImpl() {}
	private PackageStatus packageStatus = null;
	
	public static Status parseFile(String statusFile) {
		PackageStatus packageStatus = null;
		String lines[] = statusFile.split("\\r?\\n"); //the file has either Windows or Unix line endings
		String statusLine = null;
		for (String line : lines) {
			if (line.trim().startsWith("Status")) {
				statusLine = line;
				break;
			}
		}
		if (statusLine == null) {
			logger.error("Could not find status in status file.");
			packageStatus = null;
		} else {
			String statusText = statusLine.substring(statusLine.indexOf(':')+1).trim().toLowerCase();
			switch (statusText) {
			case "error":
				packageStatus = PackageStatus.ERROR;
				break;
			case "signed":
				packageStatus = PackageStatus.SIGNED;
				break;
			default:
				logger.trace("Status found in file: '"+statusText+"' - mapping to PREPARING.");
				packageStatus = PackageStatus.PREPARING;
				break;
			}
		}
		
		StatusImpl status = new StatusImpl();
		status.packageStatus = packageStatus;
		return status;
	}

	@Override
	public PackageStatus getPackageStatus() {
		return packageStatus;
	}
	
}
