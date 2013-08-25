package com.newpecunia.unicredit.webdav.impl;

import org.junit.Assert;
import org.junit.Test;

public class UnicreditFileNameResolverTest {
	
	private String SOME_ID = "01234567890123456789012345678901";
	
	@Test
	public void testIdUniqueness() {
		UnicreditFileNameResolver fnr = new UnicreditFileNameResolver();
		String id1 = fnr.generateNewId();
		System.out.println("ID1: "+id1);
		String id2 = fnr.generateNewId();
		System.out.println("ID2: "+id2);
		Assert.assertNotEquals("ID must never be equal", id1, id2);
	}
	
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
		
		String anotherId = fnr.generateNewId();
		Assert.assertEquals(id.length(), anotherId.length());		
	}

}
