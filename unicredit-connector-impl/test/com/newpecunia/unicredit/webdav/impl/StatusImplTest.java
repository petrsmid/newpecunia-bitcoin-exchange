package com.newpecunia.unicredit.webdav.impl;

import org.junit.Assert;
import org.junit.Test;

import com.newpecunia.unicredit.webdav.Status;
import com.newpecunia.unicredit.webdav.Status.PackageStatus;

public class StatusImplTest {
	
	private String testFile = 
			"XYZ\n" +
			"Status : Signed\n" +
			"aeiouy";
	
	@Test
	public void testStatusParsing() {
		Status status = StatusImpl.parseFile(testFile);
		Assert.assertEquals(PackageStatus.SIGNED, status.getPackageStatus());
	}

}
