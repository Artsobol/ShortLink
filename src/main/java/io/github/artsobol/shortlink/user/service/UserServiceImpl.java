package io.github.artsobol.shortlink.user.service;

import io.github.artsobol.shortlink.user.dto.UserCreateRequest;
import io.github.artsobol.shortlink.user.entity.Role;
import io.github.artsobol.shortlink.exception.http.UnauthorizedException;
import io.github.artsobol.shortlink.user.entity.User;
import io.github.artsobol.shortlink.user.repository.UserRepository;
import io.github.artsobol.shortlink.shortlink.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper userMapper;

    @Transactional
    public User createUser(UserCreateRequest request) {
        User user = userMapper.toEntity(request);
        user.setActive(true);
        user.setRoles(Set.of(Role.USER));
        return repository.save(user);
    }

    @Override
    public User getUserByUsername(String username) {
        return repository.findByUsername(username).orElseThrow(() -> new UnauthorizedException("user.not-found"));
    }
}
