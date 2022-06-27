package com.mike.cathaybk.interview.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mike.cathaybk.interview.model.CcyViewObject;
import com.mike.cathaybk.interview.model.CoinDeskModel;
import com.mike.cathaybk.interview.service.CoinDeskService;

@RestController
@RequestMapping(value = "/coin", produces = MediaType.APPLICATION_JSON_VALUE)
public class CoinDeskController {
	
	private final String url = "https://api.coindesk.com/v1/bpi/currentprice.json";

	@Autowired
	private CoinDeskService coinDeskService;
	
	@GetMapping(path = "/callapi")
	public ResponseEntity<CoinDeskModel> getApiData() {
		String apiReturnData = coinDeskService.getAPIdata(url);
		JSONObject jsonObj = JSON.parseObject(apiReturnData);
		CoinDeskModel model = coinDeskService.parseContentToModel(jsonObj);
		return ResponseEntity.ok().body(model);
	}
	
	@GetMapping(path = "/updateTime")
	public ResponseEntity<String> getUpdateTime() {
		String apiReturnData = coinDeskService.getAPIdata(url);
		JSONObject jsonObj = JSON.parseObject(apiReturnData);
		CoinDeskModel model = coinDeskService.parseContentToModel(jsonObj);
		return ResponseEntity.ok().body(model.getTime().get("updated"));
	}
	
	@GetMapping(path = "/ccyDetails")
	public ResponseEntity<List<CcyViewObject>> getCcyDetails() {
		String apiReturnData = coinDeskService.getAPIdata(url);
		JSONObject jsonObj = JSON.parseObject(apiReturnData);
		CoinDeskModel model = coinDeskService.parseContentToModel(jsonObj);
		return ResponseEntity.ok().body(coinDeskService.getCcyDetails(model));
	}

}
