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
	    Ccy ccyInfo = new Ccy("TWD", "台幣", new BigDecimal(10.12345), Timestamp.valueOf("2022-06-26 00:00:00"));
	    ccyService.insertCcyInfo(ccyInfo);
	    when(ccyService.getCcyInfo(ccyInfo.getCode())).thenReturn(ccyInfo);
		mockMvc.perform(get("/api/ccy/{code}", ccyInfo.getCode()).accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value(ccyInfo.getCode()))
				.andExpect(jsonPath("$.cnName").value(ccyInfo.getCnName()))
				.andExpect(jsonPath("$.rate").value(ccyInfo.getRate()))
				.andExpect(jsonPath("$.updateTime").value(ccyInfo.getUpdateTime().toLocalDateTime()))
				.andDo(print());
	}
	
	@Test
	void insertCcyInfoTest() throws Exception {
	    Ccy ccyInfo = new Ccy("USD", "美金", new BigDecimal(12.32145), Timestamp.valueOf(LocalDateTime.now()));
		mockMvc.perform(post("/api/ccy").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(JSONObject.toJSONString(ccyInfo)))
				.andExpect(status().isCreated())
				.andDo(print());
	}

	@Test
	void replaceCcyInfoTest() throws Exception {
	    Ccy oldCcyInfo = new Ccy("USD", "美金", new BigDecimal(12.32145), Timestamp.valueOf("2022-06-26 00:00:00"));
	    Ccy newCcyInfo = new Ccy("USD", "台幣", new BigDecimal(10.11111), Timestamp.valueOf("2022-06-26 21:00:00"));
	    ccyService.insertCcyInfo(oldCcyInfo);
	    when(ccyService.replaceCcyInfo(oldCcyInfo.getCode(), newCcyInfo)).thenReturn(newCcyInfo);
		mockMvc.perform(put("/api/ccy/{code}", newCcyInfo.getCode()).contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(JSONObject.toJSONString(newCcyInfo)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value(oldCcyInfo.getCode()))
				.andExpect(jsonPath("$.cnName").value(newCcyInfo.getCnName()))
				.andExpect(jsonPath("$.rate").value(newCcyInfo.getRate()))
				.andExpect(jsonPath("$.updateTime").value(newCcyInfo.getUpdateTime().toLocalDateTime()))
				.andDo(print());
	}

	@Test
	void deleteCcyInfoTest() throws Exception {
	    Ccy ccyInfo = new Ccy("GBP", "英鎊", new BigDecimal(22.22222), Timestamp.valueOf(LocalDateTime.now()));
	    ccyService.insertCcyInfo(ccyInfo);
	    
		mockMvc.perform(delete("/api/ccy/{code}", ccyInfo.getCode()))
				.andExpect(status().isNoContent())
				.andDo(print());
	}
}
