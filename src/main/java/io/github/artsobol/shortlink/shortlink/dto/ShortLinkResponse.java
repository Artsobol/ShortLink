package io.github.artsobol.shortlink.shortlink.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response with short link", example = "https://api.example.com/r/D6xg1F")
public record ShortLinkResponse(
        @Schema(description = "Original URL", example = "https://google.com") String originalUrl,
        @Schema(description = "Unique short link code", example = "D6xg1F") String code
) {
}
