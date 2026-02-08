package io.github.artsobol.shortlink.service.api;

import io.github.artsobol.shortlink.entity.User;
import io.github.artsobol.shortlink.entity.dto.UserCreateRequest;

public interface UserService {

    User createUser(UserCreateRequest request);

    User getUserByUsername(String username);

}
