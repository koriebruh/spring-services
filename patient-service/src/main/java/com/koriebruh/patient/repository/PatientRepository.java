package com.koriebruh.patient.repository;

import com.koriebruh.patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PatientRepository extends JpaRepository<Patient, Long> {

    boolean existsByEmail(String email);
//
//    boolean existsById(Long id);
}
