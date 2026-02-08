package io.github.artsobol.shortlink.entity.dto;

public record UserCreateRequest(
        String username,
        String email,
        String password
) {
}
