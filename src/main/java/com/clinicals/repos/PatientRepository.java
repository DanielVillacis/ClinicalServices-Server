package com.clinicals.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clinicals.models.Patient;

public interface PatientRepository extends JpaRepository<Patient, Integer> {

}
