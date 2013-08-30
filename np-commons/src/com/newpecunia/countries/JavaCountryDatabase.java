package com.newpecunia.countries;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class JavaCountryDatabase implements CountryDatabase {

	List<Country> countries;
	
	public JavaCountryDatabase() {
		String[] countryCodes = Locale.getISOCountries();
		countries = new ArrayList<Country>(countryCodes.length);
		for (String countryCode : countryCodes) {
			Locale locale = new Locale("", countryCode);
			countries.add(new Country(countryCode, locale.getDisplayCountry(Locale.US)));
		}
	}
	
	@Override
	public List<Country> getListOfCountries() {
		return countries;
	}
}
