package com.newpecunia.persistence.entities;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BALANCE")
public class LastKnownBalance {
	
	public static final int CONSTANT_ID = 1;
	
	@Id
	@Column(name="ID", unique=true)
	private int id;
	
	@Column(name="BALANCE")
	private BigDecimal balance;

	public int getId() {
		return CONSTANT_ID; //the entity has only one instance - always return the same ID
	}

	public void setId(int id) {
		this.id = id;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
}
