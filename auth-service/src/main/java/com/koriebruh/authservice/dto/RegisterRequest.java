package com.koriebruh.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "can't be blank")
    @Size(min = 1, max = 70, message = "length name around 1 - 70 digit")
    private String username;

    @NotBlank(message = "password can't be blank")
    @Size(min = 8, max = 70, message = "length password around 8 - 70 digit")
    private String password;

    @NotBlank(message = "email can't be blank")
    @Email(message = "should contain email")
    private String email;
}
