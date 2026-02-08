package io.github.artsobol.shortlink.api.dto;

public record UserCreateRequest(
        String username,
        String email,
        String password
) {
}
