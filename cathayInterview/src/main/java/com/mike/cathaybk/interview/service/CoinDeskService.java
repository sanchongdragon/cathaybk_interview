package com.mike.cathaybk.interview.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.mike.cathaybk.interview.Util.Utils;
import com.mike.cathaybk.interview.model.CcyDetails;
import com.mike.cathaybk.interview.model.CcyViewObject;
import com.mike.cathaybk.interview.model.CoinDeskModel;

@Service
public class CoinDeskService {
	
	@Autowired
	private CcyService ccyService;
	@Autowired
	private RestTemplate restTemplate;
	
	/**
	 * 透過 restTemplate call api
	 * @param url
	 * @return 回傳json string
	 * */
	public String getAPIdata(String url) {
		return restTemplate.getForObject(url, String.class);
	}
	
	/**
	 * 轉換response json object成自己定義的物件
	 * @param jsonObj response json object
	 * @return 轉換後的物件
	 * */
	public CoinDeskModel parseContentToModel(JSONObject jsonObj) {
		CoinDeskModel model = new CoinDeskModel();
		
		jsonObj.forEach((k, v) -> {
			putValueInModel(model, k, v);
		});
		return model;
	}
	
	/**
	 * 取出物件裡面的幣別代號, 用代號查詢DB的幣別中文名稱
	 * @param coinDeskModel 轉換後的物件
	 * @return 包含所有幣別代號、中文、匯率的List
	 * */
	public List<CcyViewObject> getCcyDetails(CoinDeskModel coinDeskModel) {
		List<CcyViewObject> ccyVoList = new ArrayList<>();
		Map<String, CcyDetails> bpiMap = coinDeskModel.getBpi();
		bpiMap.forEach((ccyName, ccyDetails) -> {
			CcyViewObject ccyVo = new CcyViewObject();
			ccyVo.setCode(ccyName);
			ccyVo.setRate(ccyDetails.getRate());
			// 從ccyService取得ccy table的幣別中文
			ccyVo.setCnName(ccyService.getCcyInfo(ccyName).getCnName());
			ccyVoList.add(ccyVo);
		});
		return ccyVoList;
	}
	
	/**
	 * 把response轉成model
	 * */
	private void putValueInModel(CoinDeskModel model, String key, Object value) {
		if(key.equals("time")) {
			Map<String, String> timeMap = new HashMap<>();
			JSONObject jObj = (JSONObject) value; 
			jObj.forEach((timeK, timeV) -> {
				timeMap.put(timeK, transTime(timeK, timeV.toString()));
			});
			
			model.setTime(timeMap);
		} else if(key.equals("disclaimer"))
			model.setDisclaimer(String.valueOf(value));
		else if(key.equals("chartName"))
			model.setChartName(String.valueOf(value));
		else if(key.equals("bpi")) {
			Map<String, CcyDetails> ccyMap = new HashMap<>();
			JSONObject jObj = (JSONObject) value; 
			jObj.forEach((ccy, details) -> {
				JSONObject detailsObj = (JSONObject) details; // 直接取出details, 轉成jsonObject				
				CcyDetails vo = new CcyDetails();
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

	/**
	 * 按照不同格式去format日期
	 * @param needTransKey 判斷是哪種格式
	 * @param needTransTime 要被format的日期
	 * @return 最後輸出特定格式的日期 "yyyy/MM/dd HH:mm:ss"
	 * */
	private String transTime(String needTransKey, String needTransTime) {
		final DateTimeFormatter formaterISO = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
		final DateTimeFormatter formatterUTC = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm:ss z", Locale.US);
		final DateTimeFormatter formatterDUK = DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' HH:mm z", Locale.US);
		final DateTimeFormatter endFormater = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss");

		if(needTransKey.equals("updated")) {
			LocalDateTime dateTimeUTC = LocalDateTime.parse(needTransTime, formatterUTC);
			return dateTimeUTC.format(endFormater);
		} else if(needTransKey.equals("updateduk")) {
			LocalDateTime dateTimeDUK = LocalDateTime.parse(needTransTime, formatterDUK);
			return dateTimeDUK.format(endFormater);
		} else if(needTransKey.equals("updatedISO")) {
			LocalDateTime dateTimeISO = LocalDateTime.parse(needTransTime, formaterISO);
			return dateTimeISO.format(endFormater);
		} else {
			throw new IllegalArgumentException();
		}

	}
}
