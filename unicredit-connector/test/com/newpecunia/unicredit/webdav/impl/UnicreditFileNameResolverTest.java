package com.newpecunia.unicredit.webdav.impl;

import org.junit.Assert;
import org.junit.Test;

import com.newpecunia.persistence.IdGenerator;

public class UnicreditFileNameResolverTest {
	
	private String SOME_ID = IdGenerator.INSTANCE.nextId();
	
	//This is just an visual test that the name is generated correctly. Check it visually.
	@Test
	public void testGetUploadFileNameForId() {
		UnicreditFileNameResolver fnr = new UnicreditFileNameResolver();
		String UploadFilename = fnr.getUploadFileNameForId(SOME_ID);
		System.out.println(UploadFilename);
	}
	
	@Test
	public void testGetIdFromStatusFile() {
		UnicreditFileNameResolver fnr = new UnicreditFileNameResolver();
		String id = fnr.getIdFromStatusFile("12312123-2013-08-25_16-23-11_"+SOME_ID+".csv.txt");
		Assert.assertEquals(SOME_ID, id);
	}

	@Test
	public void testGetIdFromStatusFileInvalid() {
		UnicreditFileNameResolver fnr = new UnicreditFileNameResolver();		
		String id = fnr.getIdFromStatusFile("12312123-2013-08-25_16-23-11_BAD_ID.csv.txt");
		Assert.assertNull(id);
	}
	
	@Test
	public void testGetIdFromUploadFile() {
		UnicreditFileNameResolver fnr = new UnicreditFileNameResolver();
		String id = fnr.getIdFromUploadFile("2013-08-25_16-26-21_"+SOME_ID+".csv");
		Assert.assertEquals(SOME_ID, id);
	}
	
	@Test
	public void testGetIdFromUploadFileInvalid() {
		UnicreditFileNameResolver fnr = new UnicreditFileNameResolver();		
		String id = fnr.getIdFromUploadFile("2013-08-25_16-26-21_BAD_ID.csv");
		Assert.assertNull(id);
	}
	
	
	@Test
	public void testCreateUploadFileName() {
		UnicreditFileNameResolver fnr = new UnicreditFileNameResolver();
		String uploadFileName = fnr.createNewUploadFileName();
		String id = fnr.getIdFromUploadFile(uploadFileName);
		
		String anotherId = IdGenerator.INSTANCE.nextId();
		Assert.assertEquals(id.length(), anotherId.length());		
	}

}
