package io.github.artsobol.shortlink.user.dto;

public record UserCreateRequest(
        String username,
        String email,
        String password
) {
}
