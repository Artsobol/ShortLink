package io.github.artsobol.shortlink.controller;

import io.github.artsobol.shortlink.entity.dto.AuthResponse;
import io.github.artsobol.shortlink.entity.dto.LoginRequest;
import io.github.artsobol.shortlink.entity.dto.RegistrationRequest;
import io.github.artsobol.shortlink.service.api.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegistrationRequest request) {
        AuthResponse response = authService.register(request);

        return getResponse(getResponseCookie(response), response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);

        return getResponse(getResponseCookie(response), response);
    }

    private static @NonNull ResponseCookie getResponseCookie(AuthResponse response) {
        return ResponseCookie.from("refresh_token", response.refreshToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .path("/api/auth/refresh")
                .maxAge(30L * 24 * 60 * 60)
                .build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @CookieValue(value = "refresh_token", required = false) String refreshToken
    ) {
        AuthResponse response = authService.refresh(refreshToken);

        return getResponse(getResponseCookie(response), response);
    }

    private static @NonNull ResponseEntity<AuthResponse> getResponse(
            ResponseCookie refreshCookie,
            AuthResponse response
    ) {
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(response.withoutRefreshToken());
    }

}
