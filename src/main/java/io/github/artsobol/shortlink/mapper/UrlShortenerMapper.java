package io.github.artsobol.shortlink.mapper;

import io.github.artsobol.shortlink.entity.ShortUrl;
import io.github.artsobol.shortlink.entity.dto.ResponseShortUrl;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UrlShortenerMapper {

    ResponseShortUrl toDto(ShortUrl responseShortUrl);
}
