package io.github.artsobol.shortlink.service.api;

import io.github.artsobol.shortlink.entity.User;

public interface RefreshTokenService {

    String createRefreshToken(User user);
}
