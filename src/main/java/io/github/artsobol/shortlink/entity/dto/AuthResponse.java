package io.github.artsobol.shortlink.entity.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        UserInfo user
) {

    public AuthResponse withoutRefreshToken() {
        return new AuthResponse(accessToken, null, user);
    }
}
