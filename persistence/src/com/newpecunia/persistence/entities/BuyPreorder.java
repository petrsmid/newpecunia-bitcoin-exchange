package com.newpecunia.persistence.entities;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "BUY_PREORDERS")
public class BuyPreorder {

	public enum BuyPreorderResolution { OPEN, PAYMENT_OK, PAYMENT_ERROR }
	
	@Id
//	@GeneratedValue(generator="uuid")
//	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@GenericGenerator(name="np_id_generator", strategy="com.newpecunia.persistence.HibernateIdGenerator")
	@GeneratedValue(generator="np_id_generator")	
	@Column(name = "ID", unique=true)
	private String id;
	
	@Column(name="BTC_AMOUNT")
	private BigDecimal btcAmount;
	
	@Column(name="USD_AMOUNT")
	private BigDecimal usdAmount;
	
	@Column(name="BTC_ADDRESS")
	private String btcAddress;
	
	@Column(name="EMAIL")
	private String email;
	
	@Column(name="CARD_TRANSACTION_ID")
	private String cardTransactionId;
	
	@Column(name="STATUS")
	@Enumerated(EnumType.STRING)
	private BuyPreorderResolution status;

	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigDecimal getBtcAmount() {
		return btcAmount;
	}

	public void setBtcAmount(BigDecimal btcAmount) {
		this.btcAmount = btcAmount;
	}

	public BigDecimal getUsdAmount() {
		return usdAmount;
	}

	public void setUsdAmount(BigDecimal usdAmount) {
		this.usdAmount = usdAmount;
	}

	public String getBtcAddress() {
		return btcAddress;
	}

	public void setBtcAddress(String btcAddress) {
		this.btcAddress = btcAddress;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCardTransactionId() {
		return cardTransactionId;
	}

	public void setCardTransactionId(String cardTransactionId) {
		this.cardTransactionId = cardTransactionId;
	}

	public BuyPreorderResolution getStatus() {
		return status;
	}

	public void setStatus(BuyPreorderResolution status) {
		this.status = status;
	}
	
	
	
	
}
