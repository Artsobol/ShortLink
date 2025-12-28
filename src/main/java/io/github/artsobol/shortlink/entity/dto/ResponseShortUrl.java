package io.github.artsobol.shortlink.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response with short link")
public record ResponseShortUrl(
        @Schema(description = "Original URL", example = "https://google.com") String originalUrl,
        @Schema(description = "code for original URL", example = "D6xg1F") String code
) {
}
