package io.github.artsobol.shortlink.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegistrationRequest(
        @NotBlank
        String username,
        @Email
        @NotBlank
        String email,
        @NotBlank
        String password
) {
}
