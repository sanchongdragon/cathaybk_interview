package com.mike.cathaybk.interview.service;

import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

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
import com.mike.cathaybk.interview.entity.Ccy;
import com.mike.cathaybk.interview.exception.NotFoundException;
import com.mike.cathaybk.interview.model.CcyVo;
import com.mike.cathaybk.interview.model.CoinDeskModel;
import com.mike.cathaybk.interview.repository.CcyRepository;

import ch.qos.logback.core.joran.util.beans.BeanUtil;

@Service
public class RestTemplateService {

	private static final int READ_TIMEOUT = 5000;
	private static final int CONNECT_TIMEOUT = 2000;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public String getAPIdata(String url) {

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
//				System.out.println(jObj.getTimestamp(timeK));
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
//		k: updateduk/ v: Jun 26, 2022 at 07:36 BST
//		k: updatedISO/ v: 2022-06-26T06:36:00+00:00
//		k: updated/ v: Jun 26, 2022 06:36:00 UTC
		String updatedISO = "2022-06-26T06:36:00+00:00";
		String updateduk = "Jun 26, 2022 at 07:36 BST";
		String updated = "Jun 26, 2022 06:36:00 UTC";
		
		final DateTimeFormatter formaterISO = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
		final DateTimeFormatter formatterUTC = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm:ss z", Locale.US);
		final DateTimeFormatter formatterDUK = DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' HH:mm z", Locale.US);
		final DateTimeFormatter endFormater = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss");

		LocalDateTime dateTimeISO = LocalDateTime.parse(updatedISO, formaterISO);
		System.out.println("ISO: "+dateTimeISO.format(endFormater));
		LocalDateTime dateTimeUTC = LocalDateTime.parse(updated, formatterUTC);
		System.out.println("UTC: "+dateTimeUTC.format(endFormater));
		LocalDateTime dateTimeDUK = LocalDateTime.parse(updateduk, formatterDUK);
		System.out.println("DUK: "+dateTimeDUK.format(endFormater));

	
	}
}
