package com.koriebruh.patient.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PatientResponse {

    private Long id;

    private String name;

    private String address;

    private String email;

    private String dateOfBirth;

    private String gender;

}
