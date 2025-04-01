package com.koriebruh.patient.service;

import com.koriebruh.patient.dto.PatientRequest;
import com.koriebruh.patient.dto.PatientResponse;
import com.koriebruh.patient.entity.Patient;
import com.koriebruh.patient.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PatientService {

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    private ValidationService validationService;

    public List<PatientResponse> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();

        //MAPPING USAGE BUILDER
        return patients.stream()
                .map(patient -> PatientResponse.builder()
                        .id(patient.getId())
                        .name(patient.getName())
                        .address(patient.getAddress())
                        .email(patient.getEmail())
                        .dateOfBirth(patient.getDateOfBirth())
                        .gender(patient.getGender())
                        .build())
                .collect(Collectors.toList());
    }

    public PatientResponse getPatientById(Long id) {
        Optional<Patient> patientOpt = patientRepository.findById(id);

        return patientOpt.map(patient ->
                PatientResponse.builder()
                        .id(patient.getId())
                        .name(patient.getName())
                        .address(patient.getAddress())
                        .email(patient.getEmail())
                        .dateOfBirth(patient.getDateOfBirth())
                        .gender(patient.getGender())
                        .build()
        ).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found with id: " + id));
    }


    public void createPatient(PatientRequest patientRequest) {
        validationService.validate(patientRequest);

        if (patientRepository.existsByEmail(patientRequest.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email address already in use");
        }

        Patient p = new Patient();
        p.setName(patientRequest.getName());
        p.setAddress(patientRequest.getAddress());
        p.setEmail(patientRequest.getEmail());
        p.setDateOfBirth(patientRequest.getDateOfBirth());
        p.setGender(patientRequest.getGender());
        p.setCreatedAt(Instant.now().getEpochSecond());
        patientRepository.save(p);
    }

    public void updatePatient(Long id, PatientRequest patientRequest) {

        validationService.validate(patientRequest);

        Optional<Patient> byId = patientRepository.findById(id);
        if (byId.isPresent()) {
            Patient patient = byId.get();

            patient.setName(patientRequest.getName());
            patient.setAddress(patientRequest.getAddress());
            patient.setEmail(patientRequest.getEmail());
            patient.setDateOfBirth(patientRequest.getDateOfBirth());
            patient.setGender(patientRequest.getGender());
            patient.setUpdatedAt(Instant.now().getEpochSecond());

            patientRepository.save(patient);

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found with id: " + id);
        }
    }


    public void deletePatient(Long id) {

        Optional<Patient> byId = patientRepository.findById(id);
        if (byId.isPresent()) {

            // doing soft delete
            Patient patient = byId.get();
            patient.softDeleted();
            patientRepository.save(patient);

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found with id: " + id);
        }

    }

}
