package com.newpecunia.unicredit.service.impl.entity;

import java.math.BigDecimal;

import ma.glasnost.orika.impl.DefaultMapperFactory;

import org.junit.Assert;
import org.junit.Test;

import com.newpecunia.countries.Country;
import com.newpecunia.countries.JavaCountryDatabase;
import com.newpecunia.unicredit.service.ForeignPayment;

public class ForeignPaymentMapperTest {
	
	@Test
	public void testBackAndForceMapping() {
		ForeignPaymentMapper mapper = new ForeignPaymentMapper(
				new DefaultMapperFactory.Builder().build(), new JavaCountryDatabase());
		
		ForeignPayment fp1 = new ForeignPayment();
		fp1.setId("someId");
		fp1.setAddress("street");
		fp1.setAmount(BigDecimal.TEN);
		fp1.setBankCountry(new Country("CZ", "Czech Republic"));
		
		ForeignPaymentOrder fpo = mapper.mapToOrder(fp1);
		ForeignPayment fp2 = mapper.mapToPayment(fpo);
		Assert.assertEquals(fp1.getId(), fp2.getId());
		Assert.assertEquals(fp1.getAddress(), fp2.getAddress());
		Assert.assertEquals(0, BigDecimal.TEN.compareTo(fp2.getAmount()));
		Assert.assertEquals(fp1.getBankCountry(), fp2.getBankCountry());
	}

}
