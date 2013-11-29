package com.newpecunia.persistence.entities;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BITSTAMP_WITHDRAW_RESTS")
public class BitstampWithdrawalRests {
	
	public static final int CONSTANT_ID = 1;

	@Id
	@Column(name="ID", unique=true)
	private int id;

	@Column(name="BTCS_TO_WITHDRAW")
	private BigDecimal btcsToWithdraw;

	@Column(name="USDS_TO_WITHDRAW")
	private BigDecimal usdsToWithdraw;

	public int getId() {
		return CONSTANT_ID; //the entity has only one instance - always return the same ID
	}

	public void setId(int id) {
		this.id = id;
	}

	public BigDecimal getBtcsToWithdraw() {
		return btcsToWithdraw;
	}

	public void setBtcsToWithdraw(BigDecimal btcsToWithdraw) {
		this.btcsToWithdraw = btcsToWithdraw;
	}

	public BigDecimal getUsdsToWithdraw() {
		return usdsToWithdraw;
	}

	public void setUsdsToWithdraw(BigDecimal usdsToWithdraw) {
		this.usdsToWithdraw = usdsToWithdraw;
	}	
	
	
	
}
