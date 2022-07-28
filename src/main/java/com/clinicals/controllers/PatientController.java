package com.clinicals.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clinicals.models.ClinicalData;
import com.clinicals.models.Patient;
import com.clinicals.repos.PatientRepository;

@RestController
@RequestMapping("/api")
public class PatientController {

	// Restful controller API
	private PatientRepository repository;

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

		ArrayList<ClinicalData> duplicateClinicalData = new ArrayList<>(clinicalData);

		for (ClinicalData eachEntry : duplicateClinicalData) {
			if (eachEntry.getComponentName().equals("hw")) {

				String[] heightAndWeight = eachEntry.getComponentValue().split("/");

				if (heightAndWeight != null && heightAndWeight.length > 1) {

					float heightInMeters = Float.parseFloat(heightAndWeight[0]) * 0.4536F;
					float bmi = Float.parseFloat(heightAndWeight[1]) / (heightInMeters * heightInMeters);
					ClinicalData bmiData = new ClinicalData();
					bmiData.setComponentName("bmi");
					bmiData.setComponentValue(Float.toString(bmi));
					clinicalData.add(bmiData);
				}
			}
		}
		return patient;
	}

}
