package io.github.artsobol.shortlink.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.List;

@Schema(description = "Validation error response")
public record ValidationErrorResponse(
        @Schema(description = "Timestamp (UTC)", example = "2025-12-28T09:15:30.123Z")
        OffsetDateTime timestamp,
        @Schema(description = "HTTP status code", example = "400")
        int status,
        @Schema(description = "HTTP status message", example = "Not Found")
        String error,
        @Schema(description = "Error message", example = "Code not found")
        String message,
        @Schema(description = "Path", example = "/r/invalidCode")
        String path,
        @Schema(description = "Validation errors", example = "[{\"field\":\"code\",\"message\":\"Code not found\"}]")
        List<ValidationFieldError> errors
) {
}


