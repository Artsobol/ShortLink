package io.github.artsobol.shortlink.auth.service;

import io.github.artsobol.shortlink.security.jwt.JwtTokenProvider;
import io.github.artsobol.shortlink.user.entity.Role;
import io.github.artsobol.shortlink.user.entity.User;
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
