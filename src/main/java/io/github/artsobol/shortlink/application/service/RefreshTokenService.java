package io.github.artsobol.shortlink.application.service;

import io.github.artsobol.shortlink.infrastructure.persistence.jpa.entity.User;

public interface RefreshTokenService {

    String createRefreshToken(User user);
}
