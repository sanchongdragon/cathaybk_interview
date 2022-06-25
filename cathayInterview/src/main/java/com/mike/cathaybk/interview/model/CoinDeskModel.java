package com.mike.cathaybk.interview.model;

import java.sql.Timestamp;
import java.util.Map;

public class CoinDeskModel {

	private Map<String, String> time;
	private String disclaimer;
	private String chartName;
	private Map<String, CcyVo> bpi;
	public Map<String, String> getTime() {
		return time;
	}
	public void setTime(Map<String, String> time) {
		this.time = time;
	}
	public String getDisclaimer() {
		return disclaimer;
	}
	public void setDisclaimer(String disclaimer) {
		this.disclaimer = disclaimer;
	}
	public String getChartName() {
		return chartName;
	}
	public void setChartName(String chartName) {
		this.chartName = chartName;
	}
	public Map<String, CcyVo> getBpi() {
		return bpi;
	}
	public void setBpi(Map<String, CcyVo> bpi) {
		this.bpi = bpi;
	}

}
