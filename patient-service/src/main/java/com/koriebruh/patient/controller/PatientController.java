package com.koriebruh.patient.controller;


import com.koriebruh.patient.dto.PatientRequest;
import com.koriebruh.patient.dto.PatientResponse;
import com.koriebruh.patient.dto.WebResponse;
import com.koriebruh.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    PatientService patientService;

    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<String>> addPatient(@RequestBody PatientRequest request) {
        patientService.createPatient(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(WebResponse.<String>builder()
                        .status("CREATED")
                        .data("Patient Created Successfully")
                        .build());
    }


    @PutMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<String>> updatePatient(@RequestBody PatientRequest request, @PathVariable Long id) {
        patientService.updatePatient(id, request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(WebResponse.<String>builder()
                        .status("UPDATED")
                        .data("Patient Updated Successfully")
                        .build());
    }


    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<PatientResponse>> getPatients(@PathVariable long id) {
        PatientResponse patientById = patientService.getPatientById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(WebResponse.<PatientResponse>builder()
                        .status("OK")
                        .data(patientById)
                        .build());
    }


    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<PatientResponse>> getAllPatients() {
        List<PatientResponse> allPatients = patientService.getAllPatients();
        return WebResponse.<List<PatientResponse>>builder()
                .status("OK")
                .data(allPatients).build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<WebResponse<String>> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(WebResponse.<String>builder()
                        .status("DELETED")
                        .data("Patient Deleted Successfully")
                        .build());
    }

}

