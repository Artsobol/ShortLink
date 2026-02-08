package io.github.artsobol.shortlink.application.service;

import io.github.artsobol.shortlink.api.dto.UserCreateRequest;
import io.github.artsobol.shortlink.infrastructure.persistence.jpa.entity.User;

public interface UserService {

    User createUser(UserCreateRequest request);

    User getUserByUsername(String username);

}
