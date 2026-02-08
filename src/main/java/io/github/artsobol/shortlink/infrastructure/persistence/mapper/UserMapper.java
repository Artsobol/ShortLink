package io.github.artsobol.shortlink.infrastructure.persistence.mapper;

import io.github.artsobol.shortlink.api.dto.UserCreateRequest;
import io.github.artsobol.shortlink.infrastructure.persistence.jpa.entity.User;
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
