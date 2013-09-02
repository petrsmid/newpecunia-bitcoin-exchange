package com.newpecunia.countries;

import java.util.List;

public interface CountryDatabase {

	List<Country> getListOfCountries();
	Country getCountryForISO(String isoCode);

}
