package com.redue.newsflow.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SignUpDto(

        @NotBlank
        String username,

        @NotBlank
        String name,

        @Email(message = "This email is invalid or use")
        @NotBlank
        String email,

        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,16}$",
                message = "password must be min 4 and max 12 length containing at least 1 uppercase, 1 lowercase, 1 special character and 1 digit ")
        @NotBlank
        String password,

        String Country) {
}
