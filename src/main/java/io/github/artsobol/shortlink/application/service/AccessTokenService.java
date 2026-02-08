package io.github.artsobol.shortlink.application.service;

import io.github.artsobol.shortlink.infrastructure.persistence.jpa.entity.User;

public interface AccessTokenService {

    String createAccessToken(User user);
}
