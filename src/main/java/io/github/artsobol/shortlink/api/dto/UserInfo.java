package io.github.artsobol.shortlink.api.dto;

import io.github.artsobol.shortlink.domain.model.Role;

import java.util.Set;
import java.util.UUID;

public record UserInfo(
        UUID id,
        String username,
        Set<Role> roles
) {
}
