package com.newpecunia.unicredit.service.impl.entity;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;

import com.google.inject.Inject;
import com.newpecunia.countries.CountryDatabase;
import com.newpecunia.unicredit.service.ForeignPayment;

public class ForeignPaymentMapper {

	private MapperFactory mapperFactory;
	private CountryDatabase countryDatabase;


	@Inject
	public ForeignPaymentMapper(MapperFactory mapperFactory, CountryDatabase countryDatabase) {
		this.mapperFactory = mapperFactory;
		this.countryDatabase = countryDatabase;
		
		mapperFactory.classMap(ForeignPayment.class, ForeignPaymentOrder.class)
			.exclude("country").exclude("bankCountry").byDefault()
			.register();

		mapperFactory.classMap(ForeignPaymentOrder.class, ForeignPayment.class)
		.exclude("country").exclude("bankCountry").byDefault()
		.register();
	}
	
	public ForeignPaymentOrder mapToOrder(ForeignPayment payment) {
		MapperFacade mapper = mapperFactory.getMapperFacade();
		ForeignPaymentOrder paymentOrder = mapper.map(payment, ForeignPaymentOrder.class);
		paymentOrder.setBankCountry(payment.getBankCountry() == null ? null : payment.getBankCountry().getIsoCode());
		paymentOrder.setCountry(payment.getCountry() == null ? null : payment.getCountry().getIsoCode());
		return paymentOrder;
	}
	
	public ForeignPayment mapToPayment(ForeignPaymentOrder paymentOrder) {
		MapperFacade mapper = mapperFactory.getMapperFacade();
		ForeignPayment payment = mapper.map(paymentOrder, ForeignPayment.class);
		payment.setBankCountry(countryDatabase.getCountryForISO(paymentOrder.getBankCountry()));
		payment.setCountry(countryDatabase.getCountryForISO(paymentOrder.getCountry()));
		return payment;
	}

}
