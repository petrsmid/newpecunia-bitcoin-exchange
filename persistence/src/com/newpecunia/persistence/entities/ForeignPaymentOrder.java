package com.newpecunia.persistence.entities;

import java.math.BigDecimal;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.newpecunia.unicredit.service.ForeignPayment.PayeeType;

@Entity
@Table(name = "PAYMENTS")
public class ForeignPaymentOrder {
	
	public static enum PaymentStatus {
		ERROR,
		NEW, 
		SENT_TO_WEBDAV, 
		WEBDAV_PENDING, 
		WEBDAV_ERROR, 
		WEBDAV_SIGNED, 
		IN_BANK, 
		PROCESSED, 
		REJECTED
	}
	
	@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "ID", unique=true)
	private String id;
	
	@Column(name="AMOUNT")
	private BigDecimal amount;
	
	@Column(name="CURRENCY")
	private String currency;

	//payee information
	@Column(name="PAYEE_TYPE")
	@Enumerated(EnumType.STRING)
	private PayeeType payeeType;
	@Column(name="PAYEE_NAME")
	private String name;
	@Column(name="PAYEE_ADDRESS")
	private String address; //street
	@Column(name="PAYEE_CITY")
	private String city;
	@Column(name="PAYEE_POSTALCODE")
	private String postalCode;
	@Column(name="PAYEE_COUNTRY")	
	private String country;

	//payee bank information
	@Column(name="PAYEE_ACCOUNT")
	private String accountNumber;
	@Column(name="PAYEE_SWIFT")
	private String swift; //SWIFT / BIC code of the bank
	@Column(name="PAYEE_BANK_NAME")
	private String bankName;
	@Column(name="PAYEE_BANK_ADDRESS")
	private String bankAddress; //street
	@Column(name="PAYEE_BANK_CITY")
	private String bankCity;
	@Column(name="PAYEE_BANK_POSTALCODE")
	private String bankPostalCode;
	@Column(name="PAYEE_BANK_COUNTRY")
	private String bankCountry;
	
	
	@Column(name="STATUS")
	@Enumerated(EnumType.STRING)
	private PaymentStatus status;
	
	@Column(name="CREATE_TIME", updatable=false)
	@Temporal(TemporalType.TIMESTAMP)	
	private Calendar createTimestamp;
	
	@Column(name="UPDATE_TIME")
	@Temporal(TemporalType.TIMESTAMP)	
	private Calendar updateTimestamp;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public PayeeType getPayeeType() {
		return payeeType;
	}
	public void setPayeeType(PayeeType payeeType) {
		this.payeeType = payeeType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getSwift() {
		return swift;
	}
	public void setSwift(String swift) {
		this.swift = swift;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankAddress() {
		return bankAddress;
	}
	public void setBankAddress(String bankAddress) {
		this.bankAddress = bankAddress;
	}
	public String getBankCity() {
		return bankCity;
	}
	public void setBankCity(String bankCity) {
		this.bankCity = bankCity;
	}
	public String getBankPostalCode() {
		return bankPostalCode;
	}
	public void setBankPostalCode(String bankPostalCode) {
		this.bankPostalCode = bankPostalCode;
	}
	public String getBankCountry() {
		return bankCountry;
	}
	public void setBankCountry(String bankCountry) {
		this.bankCountry = bankCountry;
	}
	public PaymentStatus getStatus() {
		return status;
	}
	public void setStatus(PaymentStatus status) {
		this.status = status;
	}
	public Calendar getCreateTimestamp() {
		return createTimestamp;
	}
	public void setCreateTimestamp(Calendar createTimestamp) {
		this.createTimestamp = createTimestamp;
	}
	public Calendar getUpdateTimestamp() {
		return updateTimestamp;
	}
	public void setUpdateTimestamp(Calendar updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}
	
	
	
}
