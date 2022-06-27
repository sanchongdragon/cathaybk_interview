package com.mike.cathaybk.interview.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.mike.cathaybk.interview.entity.Ccy;
import com.mike.cathaybk.interview.model.CoinDeskModel;
import com.mike.cathaybk.interview.service.CcyService;
import com.mike.cathaybk.interview.service.RestTemplateService;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class CcyController {
	
	@Autowired
	private CcyService ccyService;
	
	@GetMapping(path = "/ccy/{code}")
	public ResponseEntity<Ccy> getCcyInfo(@PathVariable("code") String code) {
		Ccy ccy = ccyService.getCcyInfo(code);
		return ResponseEntity.ok().body(ccy);
	}
	
	@GetMapping(path = "/ccy")
	public ResponseEntity<List<Ccy>> getAllCcyInfo() {
		List<Ccy> allCcy = ccyService.getAllCcyInfo();
		return allCcy.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok().body(allCcy);
	}

	@PostMapping(path = "/ccy")
	public ResponseEntity<Ccy> createCcyInfo(@RequestBody Ccy requestBody) {

		ccyService.insertCcyInfo(requestBody);
		URI location = ServletUriComponentsBuilder
						.fromCurrentRequest() 
						.path("/{code}") 
						.buildAndExpand(requestBody.getCode()) 
						.toUri(); 

		return ResponseEntity.created(location).build();
	}
	
	@PutMapping(path = "/ccy/{code}")
	public ResponseEntity<Ccy> replaceCcy(@PathVariable("code") String code, @RequestBody Ccy requestBody){
		Ccy ccy = ccyService.replaceCcyInfo(code, requestBody);
		return ResponseEntity.ok().body(ccy);
	}
	
	@DeleteMapping(path = "/ccy/{code}")
	public ResponseEntity<Ccy> deleteCcy(@PathVariable("code") String code){
		ccyService.deleteCcyInfo(code);
		return ResponseEntity.noContent().build();
	}


}
