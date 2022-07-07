package com.mike.cathaybk.interview.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mike.cathaybk.interview.entity.Ccy;
import com.mike.cathaybk.interview.service.CcyService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class CcyControllerTest {

	@Autowired
	private CcyService ccyService;
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	void getCcyInfoTest() throws Exception {
	    Ccy ccyInfo = new Ccy("TWD", "台幣");
	    ccyService.insertCcyInfo(ccyInfo);
		mockMvc.perform(get("/api/ccy/{code}", ccyInfo.getCode()).accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value(ccyInfo.getCode()))
				.andExpect(jsonPath("$.cnName").value(ccyInfo.getCnName()))
				.andDo(print());
	}
	
	@Test
	void insertCcyInfoTest() throws Exception {
	    Ccy ccyInfo = new Ccy("USD", "美金");
		mockMvc.perform(post("/api/ccy").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(JSONObject.toJSONString(ccyInfo)))
				.andExpect(status().isCreated())
				.andDo(print());
	}

	@Test
	void replaceCcyInfoTest() throws Exception {
	    Ccy oldCcyInfo = new Ccy("USD", "美金");
	    Ccy newCcyInfo = new Ccy("USD", "台幣");
	    ccyService.insertCcyInfo(oldCcyInfo);
	    RequestBuilder requestBuilder = MockMvcRequestBuilders
	    								.put("/api/ccy/{code}", newCcyInfo.getCode())
	    								.content(JSON.toJSONString(newCcyInfo))
	    								.contentType(MediaType.APPLICATION_JSON_VALUE);
	    
		mockMvc.perform(requestBuilder)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value(newCcyInfo.getCode()))
				.andExpect(jsonPath("$.cnName").value(newCcyInfo.getCnName()))
				.andDo(print());
	}

	@Test
	void deleteCcyInfoTest() throws Exception {
	    Ccy ccyInfo = new Ccy("GBP", "英鎊");
	    ccyService.insertCcyInfo(ccyInfo);
	    
	    RequestBuilder requestBuilder = MockMvcRequestBuilders
				.delete("/api/ccy/{code}", ccyInfo.getCode());

		mockMvc.perform(requestBuilder)
				.andExpect(status().isNoContent())
				.andDo(print());
	}
}
