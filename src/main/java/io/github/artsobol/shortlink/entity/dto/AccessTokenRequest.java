package io.github.artsobol.shortlink.entity.dto;

import java.util.Set;

public record AccessTokenRequest(
        String username,
        Set<String> roles
) {
}
