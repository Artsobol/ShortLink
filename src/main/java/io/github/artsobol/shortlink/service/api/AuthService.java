package io.github.artsobol.shortlink.service.api;

import io.github.artsobol.shortlink.entity.dto.AuthResponse;
import io.github.artsobol.shortlink.entity.dto.LoginRequest;
import io.github.artsobol.shortlink.entity.dto.RegistrationRequest;

public interface AuthService {

    AuthResponse login(LoginRequest request);

    AuthResponse register(RegistrationRequest request);

    AuthResponse refresh(String refreshToken);

}
