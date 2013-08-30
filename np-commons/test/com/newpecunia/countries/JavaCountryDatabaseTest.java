package com.newpecunia.countries;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class JavaCountryDatabaseTest {

	
	@Test
	public void testJavaCountryDatabase() {
		JavaCountryDatabase countryDb = new JavaCountryDatabase();
		List<Country> countries = countryDb.getListOfCountries();
		Assert.assertTrue(countries.size() > 200);
		Assert.assertTrue(countries.size() < 350);	
		Country swiss = null;
		for (Country country : countries) {
			Assert.assertTrue(country.getIsoCode().length() == 2);
			Assert.assertTrue(country.getEnglishName().length() > 1);
			
			if ("CH".equals(country.getIsoCode())) {
				swiss = country;
			}
			
			System.out.println(country);
		}
		
		Assert.assertEquals("Switzerland", swiss.getEnglishName());
	}
}
