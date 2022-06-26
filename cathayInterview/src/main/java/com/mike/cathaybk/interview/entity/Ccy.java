package com.mike.cathaybk.interview.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

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
	@Column(name = "RATE", nullable = false, columnDefinition = "Numeric(20, 5)")
	private BigDecimal rate;
	@Column(name = "UPDATETIME")
	private Timestamp updateTime;
	
	public Ccy() {
		
	}
	
	public Ccy(String code, String cnName, BigDecimal rate, Timestamp updateTime) {
		this.code = code;
		this.cnName = cnName;
		this.rate = rate;
		this.updateTime = updateTime;
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

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	
	@Override
	public String toString() {
		return String.format("CCY table [code = '%s', cnName = '%s', rate = '%s', updateTime = '%s']", code, cnName, rate, updateTime);
	}
	
}
