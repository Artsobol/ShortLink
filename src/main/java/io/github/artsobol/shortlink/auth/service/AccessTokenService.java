package io.github.artsobol.shortlink.auth.service;

import io.github.artsobol.shortlink.user.entity.User;

public interface AccessTokenService {

    String createAccessToken(User user);
}
