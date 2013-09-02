package com.newpecunia.persistence;

import org.junit.Assert;
import org.junit.Test;

public class IdGeneratorTest {
	
	@Test
	public void testIdGeneratorUniqueNumbers() {
		String last = IdGenerator.INSTANCE.nextId();
		for(int i = 0; i < 100000; i++) {
			String next = IdGenerator.INSTANCE.nextId();
			Assert.assertNotEquals(last, next);
			last = next;
		}	
	}
	
	@Test
	public void testIdGeneratorAlphaNumericFormat() {
		for(int i = 0; i < 10000; i++) {
			String id = IdGenerator.INSTANCE.nextId();
			Assert.assertTrue("ID "+id +" must consist of alphanumerical characters.",
					id.matches("^[a-zA-Z0-9]*$"));
		}
	}
	
	@Test
	public void testSameSizeOfIds() {
		String last = IdGenerator.INSTANCE.nextId();
		for(int i = 0; i < 10000; i++) {
			String next = IdGenerator.INSTANCE.nextId();
			Assert.assertEquals(last.length(), next.length());
			last = next;
		}
	}
	

}
