package com.newpecunia.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "BTC_ADDRESS_STATUS")
public class ReceivingBitcoinAddressStatus {

	public static enum AddressStatus {
		FREE,
		USED
	}	
	
	@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "ID", unique=true)
	private String id;
	
	@Column(name="ADDRESS")
	private String address;
	
	@Column(name="STATUS")
	@Enumerated(EnumType.STRING)
	private AddressStatus status;

	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public AddressStatus getStatus() {
		return status;
	}

	public void setStatus(AddressStatus status) {
		this.status = status;
	}
	
}
