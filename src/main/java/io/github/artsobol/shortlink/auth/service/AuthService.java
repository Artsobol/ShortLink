package io.github.artsobol.shortlink.auth.service;

import io.github.artsobol.shortlink.auth.dto.LoginRequest;
import io.github.artsobol.shortlink.auth.dto.AuthResponse;
import io.github.artsobol.shortlink.auth.dto.RegistrationRequest;

public interface AuthService {

    AuthResponse login(LoginRequest request);

    AuthResponse register(RegistrationRequest request);

    AuthResponse refresh(String refreshToken);

}
