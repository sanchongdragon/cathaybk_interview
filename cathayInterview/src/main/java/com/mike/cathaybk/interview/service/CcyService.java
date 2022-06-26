package com.mike.cathaybk.interview.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mike.cathaybk.interview.entity.Ccy;
import com.mike.cathaybk.interview.exception.NotFoundException;
import com.mike.cathaybk.interview.repository.CcyRepository;

@Service
public class CcyService {

	@Autowired
	private CcyRepository ccyRepository;

	public Ccy getCcyInfo(String code) {
		System.out.printf("getCcyInfo method ccy code : %s %n", code);
		Optional<Ccy> optional = ccyRepository.findByCodeIgnoreCase(code);
		return optional.orElseThrow(() -> new NotFoundException("cant find this ccy code : " + code));
	}

	public List<Ccy> getAllCcyInfo() {
		System.out.printf("getAllCcyInfo method %n");
		List<Ccy> allCcyInfo = ccyRepository.findAll();
		return allCcyInfo;
	}

	public void insertCcyInfo(Ccy ccyInfo) {
		System.out.printf("insertCcyInfo method %n");
		
		Ccy ccy = new Ccy();
		ccy.setCode(ccyInfo.getCode());
		ccy.setCnName(ccyInfo.getCnName());
		ccy.setRate(ccyInfo.getRate());
		ccy.setUpdateTime(ccyInfo.getUpdateTime());

		ccyRepository.save(ccy);
	}
	
	public Ccy replaceCcyInfo(String code, Ccy request) {
		System.out.printf("replaceCcyInfo method ccy code : %s %n", code);
		Ccy oldCcyInfo = getCcyInfo(code);
		
		Ccy newCcyInfo = new Ccy();
		newCcyInfo.setCode(oldCcyInfo.getCode());
		newCcyInfo.setCnName(request.getCnName());
		newCcyInfo.setRate(request.getRate());
		newCcyInfo.setUpdateTime(request.getUpdateTime());
		
		return ccyRepository.save(newCcyInfo);
	}

	public void deleteCcyInfo(String code) {
		System.out.printf("deleteCcyInfo method ccy code : %s %n", code);
		ccyRepository.deleteByCode(code);
	}

	
}