package io.github.artsobol.shortlink.application.service;

import io.github.artsobol.shortlink.api.dto.AuthResponse;
import io.github.artsobol.shortlink.api.dto.LoginRequest;
import io.github.artsobol.shortlink.api.dto.RegistrationRequest;

public interface AuthService {

    AuthResponse login(LoginRequest request);

    AuthResponse register(RegistrationRequest request);

    AuthResponse refresh(String refreshToken);

}
