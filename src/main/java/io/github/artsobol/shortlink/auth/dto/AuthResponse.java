package io.github.artsobol.shortlink.auth.dto;

import io.github.artsobol.shortlink.user.entity.Role;

import java.util.Set;
import java.util.UUID;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        UserInfo user
) {

    public record UserInfo(
            UUID id,
            String username,
            Set<Role> roles
    ) {
    }

    public AuthResponse withoutRefreshToken() {
        return new AuthResponse(accessToken, null, user);
    }
}
