package com.newpecunia.unicredit.webdav;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.joda.time.DateTime;

public class MulticashForeignPayment {
	
	public static enum Expenses {
		SHARED("BN1"), PAYER("OUR"), PAYEE("BN2");
		private String code;
		Expenses(String code) {
			this.code = code;
		}
		public String getCode() {
			return code;
		}
	}

	public static enum Reference {
		R_00("00"), R_01("01"), R_02("02"), R_04("04"), R_06("06"), R_07("07"), R_09("09"), R_10("10");
		private String code;
		Reference(String code) {
			this.code = code;
		}
		public String getCode() {
			return code;
		}
	}
	
	@Size(max=16)
	@Pattern(regexp="^[a-zA-Z0-9]*$")
	private String referenceId;

	@NotNull
	private DateTime date;
	
	@NotNull
	@Size(max=3)
	@Pattern(regexp="^[a-zA-Z0-9]*$")
	private String currency;

	@NotNull
	@Min(value=0)  //maximal length: 15,2
	private BigDecimal amount;

	@NotNull
	@Size(max=35)
	private String payerInfo1;
	@Size(max=35)
	private String payerInfo2;
	@Size(max=35)
	private String payerInfo3;
	@Size(max=35)
	private String payerInfo4;

	@NotNull
	@Pattern(regexp="^[0-9]*$")
	@Size(max=16)
	private String payerAccountNumber;
	
	@NotNull
	@Pattern(regexp="^[0-9]*$")
	@Size(max=16)
	private String chargeAccountNumber;  //TODO what is this?
	
	@NotNull
	@Size(max=3)
	@Pattern(regexp="^[0-9]*$")
	private String payerAccountCurrency;

	@NotNull
	@Size(max=3)
	@Pattern(regexp="^[0-9]*$")
	private String chargeAccountCurrency; //TODO what is this?
	
	@NotNull
	@Size(max=3)
	private String statisticalCode;
	
	@NotNull
	@Size(max=3)
	private String payeeCountryCode;  //TODO use ISO country instead

	@NotNull
	@Size(max=3)
	private String payeeBankCountryCode; //TODO use ISO country instead

	@NotNull
	@Size(max=11)
	private String payeeBankSwift;

	@NotNull
	@Size(max=35)
	private String payeeBankName;

	@NotNull
	@Size(max=35)
	private String payeeBankStreet;

	@NotNull
	@Size(max=35)
	private String payeeBankCity;
	
	@NotNull
	@Pattern(regexp="^[a-zA-Z0-9]*$")
	@Size(max=34)
	private String payeeAccountNumber;	
	
	@NotNull
	@Size(max=35)
	private String payeeName1;

	@Size(max=35)
	private String payeeName2;

	@Size(max=35)
	private String payeeAddres1;
	
	@Size(max=35)
	private String payeeAddres2;
	
	@Size(max=35)
	private String paymentDetail1;

	@Size(max=35)
	private String paymentDetail2;

	@Size(max=35)
	private String paymentDetail3;

	@Size(max=35)
	private String paymentDetail4;
	
	@NotNull
	private Expenses expenses;
	
	@NotNull
	@Size(min=1, max=4)
	private List<Reference> references;
	
	@NotNull
	@Size(max=35)
	private String payerContactPerson;  //TODO what is this?

	@NotNull
	@Size(max=35)
	private String extendedText; //TODO what is this?
	
	@Size(max=35)
	private String supplementaryInfo1;

	@Size(max=35)
	private String supplementaryInfo2;

	@Size(max=35)
	private String supplementaryInfo3;
	
	
	public boolean validate() {
        ValidatorFactory validationFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = validationFactory.getValidator();
		validator.validate(this);
		return true;
	}
}
