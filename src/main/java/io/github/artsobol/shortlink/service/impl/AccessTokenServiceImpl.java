package io.github.artsobol.shortlink.service.impl;

import io.github.artsobol.shortlink.entity.Role;
import io.github.artsobol.shortlink.entity.User;
import io.github.artsobol.shortlink.security.jwt.JwtTokenProvider;
import io.github.artsobol.shortlink.service.api.AccessTokenService;
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
