package io.github.artsobol.shortlink.api.error;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Validation error")
public record ValidationFieldError(
        @Schema(description = "Field name", example = "code")
        String field,
        @Schema(description = "Error message", example = "Code not found")
        String message
) {
}
