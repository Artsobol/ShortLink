package io.github.artsobol.shortlink.mapper;

import io.github.artsobol.shortlink.entity.ShortLink;
import io.github.artsobol.shortlink.entity.dto.ShortLinkResponse;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ShortLinkMapper {

    ShortLinkResponse toDto(ShortLink responseShortLink);
}
