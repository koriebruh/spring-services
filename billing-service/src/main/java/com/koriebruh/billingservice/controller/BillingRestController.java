package com.koriebruh.billingservice.controller;

import com.koriebruh.billingservice.dto.BillingResponseDTO;
import com.koriebruh.billingservice.dto.BillingRequestDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/billing")
public class BillingRestController {

    @Autowired
    private Validator validator;

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String>Test() {
        return ResponseEntity.ok("Billing Service is up and running");
    }

    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<BillingResponseDTO> createBillingAccount(@RequestBody BillingRequestDTO request) {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(request);
        if (constraintViolations.size() != 0) {
            throw new ConstraintViolationException(constraintViolations);
        }
        // Logic to create a billing account, next time will be updated

        BillingResponseDTO response = BillingResponseDTO.builder()
                .accountId(request.getPatientId())
                .status("ACTIVE")
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);

    }
}
