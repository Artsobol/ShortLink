package io.github.artsobol.shortlink.service.impl;

import io.github.artsobol.shortlink.entity.RefreshToken;
import io.github.artsobol.shortlink.entity.User;
import io.github.artsobol.shortlink.entity.dto.AuthResponse;
import io.github.artsobol.shortlink.entity.dto.LoginRequest;
import io.github.artsobol.shortlink.entity.dto.RegistrationRequest;
import io.github.artsobol.shortlink.entity.dto.UserCreateRequest;
import io.github.artsobol.shortlink.entity.dto.UserInfo;
import io.github.artsobol.shortlink.exception.ConflictException;
import io.github.artsobol.shortlink.exception.UnauthorizedException;
import io.github.artsobol.shortlink.repository.RefreshTokenRepository;
import io.github.artsobol.shortlink.repository.UserRepository;
import io.github.artsobol.shortlink.service.api.AccessTokenService;
import io.github.artsobol.shortlink.service.api.AuthService;
import io.github.artsobol.shortlink.service.api.RefreshTokenService;
import io.github.artsobol.shortlink.utils.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccessTokenService accessTokenService;
    private final UserServiceImpl userService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userService.getUserByUsername(request.username());
        ensureCredentialsValid(request.password(), user.getPassword());

        return getAuthResponse(user);
    }

    @Override
    @Transactional
    public AuthResponse register(RegistrationRequest request) {
        ensureUsernameNotExists(request.username());
        ensureEmailNotExists(request.email());

        User user = userService.createUser(new UserCreateRequest(
                request.username(),
                request.email(),
                encodePassword(request.password())
        ));

        return getAuthResponse(user);
    }

    @Override
    @Transactional
    public AuthResponse refresh(String refreshToken) {
        RefreshToken session = refreshTokenRepository.findByTokenHash(TokenUtils.sha256Hex(refreshToken))
                .orElseThrow(() -> new UnauthorizedException("token.invalid"));

        ensureNotRevoke(session);
        ensureNotExpired(session);
        session.setRevoked(true);

        User user = session.getUser();
        return getAuthResponse(user);
    }

    private @NonNull AuthResponse getAuthResponse(User user) {
        return new AuthResponse(
                accessTokenService.createAccessToken(user),
                refreshTokenService.createRefreshToken(user),
                new UserInfo(user.getId(), user.getUsername(), user.getRoles())
        );
    }

    private static void ensureNotExpired(RefreshToken session) {
        if (Instant.now().isAfter(session.getExpiresAt())) {
            throw new UnauthorizedException("token.expired");
        }
    }

    private static void ensureNotRevoke(RefreshToken session) {
        if (session.isRevoked()) {
            throw new UnauthorizedException("token.revoked");
        }
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private void ensureCredentialsValid(String password, String confirmPassword) {
        if (!passwordEncoder.matches(password, confirmPassword)) {
            throw new UnauthorizedException("auth.bad-credentials");
        }
    }

    private void ensureUsernameNotExists(String username) {
        if (checkUsername(username)) {
            throw new ConflictException("auth.username-exists");
        }
    }

    private void ensureEmailNotExists(String email) {
        if (checkEmail(email)) {
            throw new ConflictException("auth.email-exists");
        }
    }

    private boolean checkUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    private boolean checkEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
