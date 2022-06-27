package com.mike.cathaybk.interview.model;

import java.math.BigDecimal;

public class CcyDetails {
	private String symbol;
	private String code;
	private String description; 
	private BigDecimal rateFloat;
	private BigDecimal rate;
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public BigDecimal getRate() {
		return rate;
	}
	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
	public BigDecimal getRateFloat() {
		return rateFloat;
	}
	public void setRateFloat(BigDecimal rateFloat) {
		this.rateFloat = rateFloat;
	}
	public String showContent() {
		StringBuilder builder = new StringBuilder();
		builder.append("code: ").append(code).append(" / ");
		builder.append("symbol: ").append(symbol).append(" / ");
		builder.append("rate: ").append(rate).append(" / ");
		builder.append("rate_float: ").append(rateFloat).append(" / ");
		builder.append("description: ").append(description);
		return builder.toString();
	}
}
