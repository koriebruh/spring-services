package com.koriebruh.patient.controller;


import com.koriebruh.patient.dto.PatientRequest;
import com.koriebruh.patient.dto.PatientResponse;
import com.koriebruh.patient.dto.WebResponse;
import com.koriebruh.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PatientController {

    @Autowired
    PatientService patientService;

    @PostMapping(
            path = "api/patients",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> addPatient(@RequestBody PatientRequest request) {
        patientService.createPatient(request);
        return WebResponse.<String>builder().data("successful create patient").build();
    }


    @PutMapping(
            path = "api/patients/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> updatePatient(@RequestBody PatientRequest request, @PathVariable Long id) {
        patientService.updatePatient(id, request);
        return WebResponse.<String>builder().data("successful update patient").build();
    }


    @GetMapping(
            path = "api/patients/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<PatientResponse> getPatients(@PathVariable long id) {
        PatientResponse patientById = patientService.getPatientById(id);
        return WebResponse.<PatientResponse>builder().data(patientById).build();
    }


    @GetMapping(
            path = "api/patients",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<PatientResponse>> getAllPatients() {
        List<PatientResponse> allPatients = patientService.getAllPatients();
        return WebResponse.<List<PatientResponse>>builder()
                .data(allPatients).build();
    }


    @DeleteMapping("api/patients/{id}")
    public WebResponse<String> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return WebResponse.<String>builder().data("successful delete patient").build();
    }

}
