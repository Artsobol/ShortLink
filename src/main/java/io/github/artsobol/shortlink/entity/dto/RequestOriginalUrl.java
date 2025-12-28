package io.github.artsobol.shortlink.entity.dto;

import io.github.artsobol.shortlink.validation.HttpUrl;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request for creating a short link")
public record RequestOriginalUrl(
        @NotBlank @HttpUrl @Schema(description = "Original URL", example = "https://google.com") String originalUrl
) {
}
