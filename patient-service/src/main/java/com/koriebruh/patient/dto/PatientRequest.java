package com.koriebruh.patient.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PatientRequest {

    @NotBlank @Size(min = 1, max = 70, message = "length name around 1 - 70 digit")
    private String name;

    private String address;

    @NotBlank @Email(message = "should contain email")
    private String email;

    @NotBlank(message = "date of birth can't be null")
    private String dateOfBirth;

    @NotBlank(message = "gender can't be null false for man , true for girl")
    private String gender;
}
