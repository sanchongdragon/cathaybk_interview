package com.mike.cathaybk.interview.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mike.cathaybk.interview.entity.Ccy;

@Repository
public interface CcyRepository extends JpaRepository<Ccy, String> {

	public Optional<Ccy> findByCodeIgnoreCase(String code);

	public void deleteByCode(String code);
}
