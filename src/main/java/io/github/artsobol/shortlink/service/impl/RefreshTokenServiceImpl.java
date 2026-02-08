package io.github.artsobol.shortlink.service.impl;

import io.github.artsobol.shortlink.entity.RefreshToken;
import io.github.artsobol.shortlink.entity.User;
import io.github.artsobol.shortlink.repository.RefreshTokenRepository;
import io.github.artsobol.shortlink.service.api.RefreshTokenService;
import io.github.artsobol.shortlink.utils.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository repository;

    @Value("${app.security.refresh.ttl}")
    private Duration refreshTtl;
    @Value("${app.security.refresh.pepper}")
    private String pepper;

    @Override
    @Transactional
    public String createRefreshToken(User user) {
        RefreshToken token = createTokenWithUserAndExpiresAt(user);
        String rawToken = TokenUtils.generateRawToken(64);
        token.setTokenHash(TokenUtils.hmacSha256Base64Url(rawToken, pepper));

        repository.save(token);
        return rawToken;
    }

    private RefreshToken createTokenWithUserAndExpiresAt(User user) {
        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setExpiresAt(Instant.now().plus(refreshTtl));
        return token;
    }

}
