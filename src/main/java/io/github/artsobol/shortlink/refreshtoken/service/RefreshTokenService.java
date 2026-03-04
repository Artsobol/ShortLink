package io.github.artsobol.shortlink.refreshtoken.service;

import io.github.artsobol.shortlink.user.entity.User;

public interface RefreshTokenService {

    String createRefreshToken(User user);
}
