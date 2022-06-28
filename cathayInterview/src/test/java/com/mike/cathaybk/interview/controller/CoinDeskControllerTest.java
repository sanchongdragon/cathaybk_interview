package com.mike.cathaybk.interview.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.mike.cathaybk.interview.model.CoinDeskModel;
import com.mike.cathaybk.interview.service.CoinDeskService;

@WebMvcTest(CoinDeskController.class)
class CoinDeskControllerTest {

	private final String url = "https://api.coindesk.com/v1/bpi/currentprice.json";

	@MockBean
	private CoinDeskService coinDeskService;
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private RestTemplate restTimeplate;
	
	@Test
	void getApiDataTest() throws Exception {
		final String response = "{\"time\":{\"updated\":\"Jun 28, 2022 00:16:00 UTC\",\"updatedISO\":\"2022-06-28T00:16:00+00:00\",\"updateduk\":\"Jun 28, 2022 at 01:16 BST\"},\"disclaimer\":\"This data was produced from the CoinDesk Bitcoin Price Index (USD). Non-USD currency data converted using hourly conversion rate from openexchangerates.org\",\"chartName\":\"Bitcoin\",\"bpi\":{\"USD\":{\"code\":\"USD\",\"symbol\":\"&#36;\",\"rate\":\"20,803.6514\",\"description\":\"United States Dollar\",\"rate_float\":20803.6514},\"GBP\":{\"code\":\"GBP\",\"symbol\":\"&pound;\",\"rate\":\"16,890.8798\",\"description\":\"British Pound Sterling\",\"rate_float\":16890.8798},\"EUR\":{\"code\":\"EUR\",\"symbol\":\"&euro;\",\"rate\":\"19,775.3269\",\"description\":\"Euro\",\"rate_float\":19775.3269}}}";
		when(coinDeskService.getAPIdata(url)).thenReturn(response);
		mockMvc.perform(get("/coin/callapi"))
				.andDo(print())
				.andExpect(status().isOk());
	}
	
	@Test
	void getUpdateTimeTest() throws Exception {
		final String response = "{\"time\":{\"updated\":\"Jun 28, 2022 00:16:00 UTC\",\"updatedISO\":\"2022-06-28T00:16:00+00:00\",\"updateduk\":\"Jun 28, 2022 at 01:16 BST\"},\"disclaimer\":\"This data was produced from the CoinDesk Bitcoin Price Index (USD). Non-USD currency data converted using hourly conversion rate from openexchangerates.org\",\"chartName\":\"Bitcoin\",\"bpi\":{\"USD\":{\"code\":\"USD\",\"symbol\":\"&#36;\",\"rate\":\"20,803.6514\",\"description\":\"United States Dollar\",\"rate_float\":20803.6514},\"GBP\":{\"code\":\"GBP\",\"symbol\":\"&pound;\",\"rate\":\"16,890.8798\",\"description\":\"British Pound Sterling\",\"rate_float\":16890.8798},\"EUR\":{\"code\":\"EUR\",\"symbol\":\"&euro;\",\"rate\":\"19,775.3269\",\"description\":\"Euro\",\"rate_float\":19775.3269}}}";
		when(coinDeskService.getAPIdata(url)).thenReturn(response);
		CoinDeskModel model = coinDeskService.parseContentToModel(JSON.parseObject(response));
		when(coinDeskService.parseContentToModel(JSON.parseObject(response))).thenReturn(model);
		mockMvc.perform(get("/coin/updateTime"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.chartName").value(model.getChartName()));
	}

	
}
