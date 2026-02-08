package io.github.artsobol.shortlink.application.impl;

import io.github.artsobol.shortlink.application.service.AccessTokenService;
import io.github.artsobol.shortlink.domain.model.Role;
import io.github.artsobol.shortlink.infrastructure.persistence.jpa.entity.User;
import io.github.artsobol.shortlink.infrastructure.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccessTokenServiceImpl implements AccessTokenService {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public String createAccessToken(User user) {
        Set<String> roles = user.getRoles().stream().map(Role::name).collect(Collectors.toSet());
        return jwtTokenProvider.generateToken(user.getUsername(), roles);
    }
}
