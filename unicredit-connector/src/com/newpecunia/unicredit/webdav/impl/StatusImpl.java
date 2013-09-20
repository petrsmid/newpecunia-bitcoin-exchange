package com.newpecunia.unicredit.webdav.impl;

import com.newpecunia.unicredit.webdav.Status;

public class StatusImpl implements Status {

	private StatusImpl() {}
	private PackageStatus packageStatus = null;
	
	public static Status parseFile(String statusFile) {
		StatusImpl status = new StatusImpl();
		// TODO parse the file
		return status;
	}

	@Override
	public PackageStatus getPackageStatus() {
		return packageStatus;
	}
	
}
