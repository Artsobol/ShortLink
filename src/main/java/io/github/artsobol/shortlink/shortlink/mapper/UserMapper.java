package io.github.artsobol.shortlink.shortlink.mapper;

import io.github.artsobol.shortlink.user.dto.UserCreateRequest;
import io.github.artsobol.shortlink.user.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "username", source = "username")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "password", source = "password")
    User toEntity(UserCreateRequest request);
}
