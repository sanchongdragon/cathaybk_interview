package com.mike.cathaybk.interview.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Ccy")
public class Ccy {

	@Id
	@Column(name = "CODE", length = 3)
	private String code;
	@Column(name = "CNNAME", length = 10)
	private String cnName;
	
	public Ccy() {
		
	}
	
	public Ccy(String code, String cnName) {
		this.code = code;
		this.cnName = cnName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCnName() {
		return cnName;
	}

	public void setCnName(String cnName) {
		this.cnName = cnName;
	}
	
	@Override
	public String toString() {
		return String.format("CCY table [code = '%s', cnName = '%s']", code, cnName);
	}
	
}
