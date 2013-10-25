package com.newpecunia.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BALANCE")
public class LastShortId {

	public static final int CONSTANT_ID = 1;

	@Id
	@Column(name = "ID", unique = true)
	private int id;

	@Column(name = "SHORT_ID")
	private int shortId;

	public int getId() {
		return CONSTANT_ID; // the entity has only one instance - always return
							// the same ID
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getShortId() {
		return shortId;
	}

	public void setShortId(int shortId) {
		this.shortId = shortId;
	}

}
