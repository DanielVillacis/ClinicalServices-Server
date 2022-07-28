package com.clinicals.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clinicals.models.ClinicalData;
import com.clinicals.models.Patient;
import com.clinicals.repos.PatientRepository;
import com.clinicals.util.BMICalculator;

@RestController
@RequestMapping("/api")
@CrossOrigin // To communicate between different ports with the ReactApp with AJAX calls
public class PatientController {

	// Restful controller API
	private PatientRepository repository;
	Map<String, String> filters = new HashMap<>();

	@Autowired
	PatientController(PatientRepository repository) {
		this.repository = repository;
	}

	@RequestMapping(value = "/patients", method = RequestMethod.GET)
	public List<Patient> getPatients() {
		return repository.findAll();
	}

	// GET is the default request method
	// optional but useful to declare
	@RequestMapping(value = "/patients/{id}", method = RequestMethod.GET)
	public Patient getPatient(@PathVariable("id") int id) {
		return repository.findById(id).get();
	}

	@RequestMapping(value = "/patients", method = RequestMethod.POST)
	public Patient savePatient(@RequestBody Patient patient) {
		return repository.save(patient);
	}

	// Main algorithm for the patient data analysis, will return the latest clinical
	// data to the patient (body mass index for now)
	@RequestMapping(value = "/patients/analyze/{id}", method = RequestMethod.GET)
	public Patient analyze(@PathVariable("id") int id) {

		Patient patient = repository.findById(id).get();
		List<ClinicalData> clinicalData = patient.getClinicalData();

		List<ClinicalData> duplicateClinicalData = new ArrayList<>(clinicalData);

		for (ClinicalData eachEntry : duplicateClinicalData) {
			// Simple filtering logic using a hashmap to store unique keys and remove
			// duplicates after a new data insertion
			if (filters.containsKey(eachEntry.getComponentName())) {
				clinicalData.remove(eachEntry); //
				continue;
			} else {
				filters.put(eachEntry.getComponentName(), null);
			}

			BMICalculator.calculateBMI(clinicalData, eachEntry);
		}
		filters.clear();
		return patient;
	}

}
