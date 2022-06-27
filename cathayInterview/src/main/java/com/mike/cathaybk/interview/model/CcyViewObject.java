package com.mike.cathaybk.interview.model;

import java.math.BigDecimal;

public class CcyViewObject {

	private String code;
	private String cnName;
	private BigDecimal rate;
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
}
