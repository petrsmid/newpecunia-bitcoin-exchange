package com.newpecunia.countries;

public final class Country {
	private String isoCode; //two letter code - e.g.: US, AT, CZ, ...
	private String englishName; //e.g. Austria, Czech Republic, ...
	
	
	/**
	 * Do not call the constructor manually. Always use country database.
	 * It is however public for usage in tests
	 */
	public Country(String isoCode, String englishName) {
		super();
		this.isoCode = isoCode;
		this.englishName = englishName;
	}

	public String getIsoCode() {
		return isoCode;
	}
	public String getEnglishName() {
		return englishName;
	}

	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((isoCode == null) ? 0 : isoCode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Country other = (Country) obj;
		if (isoCode == null) {
			if (other.isoCode != null)
				return false;
		} else if (!isoCode.equals(other.isoCode))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return getIsoCode() + ": " + getEnglishName();
	}
	
}