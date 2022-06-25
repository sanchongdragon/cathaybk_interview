package com.mike.cathaybk.interview.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mike.cathaybk.interview.model.CoinDeskModel;
import com.mike.cathaybk.interview.service.RestTemplateService;

@RestController
@RequestMapping(value = "/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class HttpController {

	private final String url = "https://api.coindesk.com/v1/bpi/currentprice.json";
	
	@Autowired
	private RestTemplateService restTemplateService;
	
	@GetMapping(value = "/get")
	public void get() {
		System.out.println("get");
		String coinDeskModel = restTemplateService.get(url);
//		System.out.println("coinDeskModel chartName : " + coinDeskModel.getChartName());
//		System.out.println("coinDeskModel getDisclaimer : " + coinDeskModel.getDisclaimer());
//		System.out.println(coinDeskModel);
		
	}
	
}
