package com.newpecunia.unicredit.webdav;

public interface Status {
	
	public enum PackageStatus { PREPARING, ERROR, PARTLY_SIGNED, SIGNED }
	
	PackageStatus getPackageStatus();
	
}
