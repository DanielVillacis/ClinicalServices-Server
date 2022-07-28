package com.clinicals.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clinicals.models.ClinicalData;

public interface ClinicalDataRepository extends JpaRepository<ClinicalData, Integer> {

	
}
