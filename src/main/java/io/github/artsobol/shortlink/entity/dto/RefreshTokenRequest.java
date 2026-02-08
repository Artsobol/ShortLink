package io.github.artsobol.shortlink.entity.dto;

import io.github.artsobol.shortlink.entity.User;

import java.time.Instant;

public record RefreshTokenRequest(
        User user,
        Instant expiresAt
) {
}
