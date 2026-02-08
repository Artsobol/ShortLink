package io.github.artsobol.shortlink.entity.dto;

import io.github.artsobol.shortlink.entity.Role;

import java.util.Set;
import java.util.UUID;

public record UserInfo(
        UUID id,
        String username,
        Set<Role> roles
) {
}
