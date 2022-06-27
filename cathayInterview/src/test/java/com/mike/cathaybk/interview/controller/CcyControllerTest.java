package com.mike.cathaybk.interview.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PathVariable;

import com.alibaba.fastjson.JSONObject;
import com.mike.cathaybk.interview.entity.Ccy;
import com.mike.cathaybk.interview.repository.CcyRepository;
import com.mike.cathaybk.interview.service.CcyService;

@WebMvcTest(CcyController.class)
class CcyControllerTest {

	@MockBean
	private CcyService ccyService;
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	void getCcyInfoTest() throws Exception {
	    Ccy ccyInfo = new Ccy("TWD", "台幣");
	    ccyService.insertCcyInfo(ccyInfo);
	    when(ccyService.getCcyInfo(ccyInfo.getCode())).thenReturn(ccyInfo);
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
	    when(ccyService.replaceCcyInfo(oldCcyInfo.getCode(), newCcyInfo)).thenReturn(newCcyInfo);
//	    System.out.println(JSONObject.toJSONString(newCcyInfo));
		mockMvc.perform(put("/api/ccy/{code}", newCcyInfo.getCode())
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(JSONObject.toJSONString(newCcyInfo)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value(newCcyInfo.getCode()))
				.andExpect(jsonPath("$.cnName").value(newCcyInfo.getCnName()))
				.andDo(print());
	}

	@Test
	void deleteCcyInfoTest() throws Exception {
	    Ccy ccyInfo = new Ccy("GBP", "英鎊");
	    ccyService.insertCcyInfo(ccyInfo);
	    
		mockMvc.perform(delete("/api/ccy/{code}", ccyInfo.getCode()))
				.andExpect(status().isNoContent())
				.andDo(print());
	}
}
