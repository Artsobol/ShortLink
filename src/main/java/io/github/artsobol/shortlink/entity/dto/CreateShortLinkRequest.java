package io.github.artsobol.shortlink.entity.dto;

import io.github.artsobol.shortlink.validation.HttpUrl;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request for creating a short link")
public record CreateShortLinkRequest(
        @NotBlank(message = "{validation.url.blank}")
        @HttpUrl(message = "{validation.url.invalid}")
        @Schema(description = "Original URL", example = "https://google.com")
        String originalUrl
) {
}
