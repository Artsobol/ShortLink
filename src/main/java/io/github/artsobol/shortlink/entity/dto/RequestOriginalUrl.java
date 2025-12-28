package io.github.artsobol.shortlink.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Schema(description = "Request for creating a short link")
public record RequestOriginalUrl(
        @NotBlank @URL @Schema(description = "Original URL", example = "https://google.com") String originalUrl
) {
}
