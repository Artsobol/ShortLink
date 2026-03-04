package io.github.artsobol.shortlink.user.service;

import io.github.artsobol.shortlink.user.dto.UserCreateRequest;
import io.github.artsobol.shortlink.user.entity.User;

public interface UserService {

    User createUser(UserCreateRequest request);

    User getUserByUsername(String username);

}
