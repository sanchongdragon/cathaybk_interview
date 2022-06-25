package com.mike.cathaybk.interview.service;

import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mike.cathaybk.interview.Util.Utils;
import com.mike.cathaybk.interview.apiconfig.RestTemplateConfig;
import com.mike.cathaybk.interview.model.CcyVo;
import com.mike.cathaybk.interview.model.CoinDeskModel;

import ch.qos.logback.core.joran.util.beans.BeanUtil;

@Service
public class RestTemplateService {

	private static final int READ_TIMEOUT = 5000;
	private static final int CONNECT_TIMEOUT = 2000;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public String get(String url) {
		
		String response = this.restTemplate.getForObject(url, String.class);
		JSONObject jsonObj = JSONObject.parseObject(response);
		CoinDeskModel model = this.parseContentToModel(jsonObj);

		Map<String, String> map = model.getTime();
		map.forEach((timeK, timeV) -> {
			System.out.println("k: "+timeK + "/ v: "+timeV);
		});
		
		Map<String, CcyVo> bpimap = model.getBpi();
		
		bpimap.forEach((ccy, details) -> {
			System.out.println("ccy : " + ccy);
			System.out.println(details.showContent());
		});
		
		return this.restTemplate.getForObject(url, String.class);
	}
	
	private CoinDeskModel parseContentToModel(JSONObject jsonObj) {
		CoinDeskModel model = new CoinDeskModel();
		
		jsonObj.forEach((k, v) -> {
			System.out.println(String.format("key: %s, Value: %s", k, v));
			putValueInModel(model, k, v);
		});
		return model;
	}
	
	private void putValueInModel(CoinDeskModel model, String key, Object value) {
		if(key.equals("time")) {
			Map<String, String> timeMap = new HashMap<>();
			JSONObject jObj = (JSONObject) value; 
			jObj.forEach((timeK, timeV) -> {
				System.out.println(jObj.getTimestamp(timeK));
				timeMap.put(timeK, timeV.toString());
			});
			model.setTime(timeMap);
		} else if(key.equals("disclaimer"))
			model.setDisclaimer(String.valueOf(value));
		else if(key.equals("chartName"))
			model.setChartName(String.valueOf(value));
		else if(key.equals("bpi")) {
			Map<String, CcyVo> ccyMap = new HashMap<>();
			JSONObject jObj = (JSONObject) value; 
			jObj.forEach((ccy, details) -> {
				JSONObject detailsObj = (JSONObject) details; // 直接取出details, 轉成jsonObject				
				CcyVo vo = new CcyVo();
				vo.setCode(detailsObj.getString("code"));
				vo.setDescription(detailsObj.getString("description"));
				vo.setRate(Utils.chgBigDecimal(detailsObj.getString("rate").replace(",", "")));
				vo.setRateFloat(Utils.chgBigDecimal(detailsObj.getString("rate_float").replace(",","")));
				vo.setSymbol(detailsObj.getString("symbol"));

				ccyMap.put(ccy, vo);
			});
			
			model.setBpi(ccyMap);
		}
	}
	
	public static void main(String[] argu) {
//		k: updateduk/ v: Jun 25, 2022 at 09:44 BST
//		k: updatedISO/ v: 2022-06-25T08:44:00+00:00
//		k: updated/ v: Jun 25, 2022 08:44:00 UTC
		String updatedISO = "2022-06-25T08:44:00+00:00";
		String updateduk = "Jun 25, 2022 at 09:44 BST";
		String updated = "Jun 25, 2022 08:44:00 UTC";
		DateTimeFormatter formater = DateTimeFormatter.BASIC_ISO_DATE;

		LocalDateTime dateTime = LocalDateTime.parse(updated, formater);
		System.out.println(dateTime);
//		DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss")
//		System.out.println(formater.parse(updatedISO));
//		Timestamp ts = new Timestamp();
	}
}
