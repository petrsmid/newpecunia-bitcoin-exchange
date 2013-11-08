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

@Entity
@Table(name = "BTC_ORDERS")
public class BtcPaymentOrder {

	public static enum BtcOrderStatus {
		UNPROCESSED,
		PROCESSED
	}
	
	
	@Id
//	@GeneratedValue(generator="uuid")
//	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@GenericGenerator(name="np_id_generator", strategy="com.newpecunia.persistence.HibernateIdGenerator")
	@GeneratedValue(generator="np_id_generator")	
	@Column(name = "ID", unique=true)
	private String id;
	
	@Column(name="AMOUNT")
	private BigDecimal amount;
	
	@Column(name="ADDRESS")
	private String address;

	@Column(name="STATUS")
	@Enumerated(EnumType.STRING)
	private BtcOrderStatus status;
	
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public BtcOrderStatus getStatus() {
		return status;
	}

	public void setStatus(BtcOrderStatus status) {
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
