package com.newpecunia.unicredit.service.impl.entity;

import java.math.BigDecimal;

import ma.glasnost.orika.impl.DefaultMapperFactory;

import org.junit.Assert;
import org.junit.Test;

import com.newpecunia.countries.Country;
import com.newpecunia.countries.JavaCountryDatabase;
import com.newpecunia.unicredit.service.ForeignPayment;
import com.newpecunia.unicredit.service.ForeignPayment.PayeeType;

public class ForeignPaymentMapperTest {
	
	@Test
	public void testBackAndForceMapping() {
		ForeignPaymentMapper mapper = new ForeignPaymentMapper(
				new DefaultMapperFactory.Builder().build(), new JavaCountryDatabase());
		
		ForeignPayment fp1 = new ForeignPayment();
		fp1.setAddress("street");
		fp1.setAmount(BigDecimal.TEN);
		fp1.setBankCountry(new Country("CZ", "Czech Republic"));
		fp1.setPayeeType(PayeeType.CUSTOMER);
		
		ForeignPaymentOrder fpo = mapper.mapToOrder(fp1);
		ForeignPayment fp2 = mapper.mapToPayment(fpo);
		Assert.assertEquals(fp1.getAddress(), fp2.getAddress());
		Assert.assertEquals(0, BigDecimal.TEN.compareTo(fp2.getAmount()));
		Assert.assertEquals(fp1.getBankCountry(), fp2.getBankCountry());
		Assert.assertEquals(fp1.getPayeeType(), fp2.getPayeeType());
	}

}
