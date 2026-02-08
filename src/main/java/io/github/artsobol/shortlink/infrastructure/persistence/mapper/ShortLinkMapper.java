package io.github.artsobol.shortlink.infrastructure.persistence.mapper;

import io.github.artsobol.shortlink.api.dto.ShortLinkResponse;
import io.github.artsobol.shortlink.infrastructure.persistence.jpa.entity.ShortLink;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ShortLinkMapper {

    ShortLinkResponse toDto(ShortLink responseShortLink);
}
