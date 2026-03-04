package io.github.artsobol.shortlink.user.mapper;

import io.github.artsobol.shortlink.shortlink.dto.ShortLinkResponse;
import io.github.artsobol.shortlink.shortlink.entity.ShortLink;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ShortLinkMapper {

    ShortLinkResponse toDto(ShortLink responseShortLink);
}
