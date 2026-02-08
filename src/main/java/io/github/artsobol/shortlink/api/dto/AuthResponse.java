package io.github.artsobol.shortlink.api.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        UserInfo user
) {

    public AuthResponse withoutRefreshToken() {
        return new AuthResponse(accessToken, null, user);
    }
}
