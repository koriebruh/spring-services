package com.koriebruh.billingservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
public class BillingRequestDTO {

    @NotNull
    @Min(value = 1, message = "Patient ID must be greater than 0")
    private long patientId;

    @NotBlank(message = "cannot be blank")
    @Size(min = 1, message = "minimum 1 character")
    private String name;

    @NotBlank(message = "cannot be blank")
    @NotNull(message = " cannot be null")
    private String email;
}
