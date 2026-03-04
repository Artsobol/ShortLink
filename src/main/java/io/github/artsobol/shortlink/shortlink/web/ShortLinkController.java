package io.github.artsobol.shortlink.shortlink.web;

import io.github.artsobol.shortlink.api.error.ErrorResponse;
import io.github.artsobol.shortlink.shortlink.dto.CreateShortLinkRequest;
import io.github.artsobol.shortlink.shortlink.dto.ShortLinkResponse;
import io.github.artsobol.shortlink.shortlink.service.ShortLinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static io.github.artsobol.shortlink.api.web.CreateUriUtils.buildOriginalUri;
import static io.github.artsobol.shortlink.api.web.CreateUriUtils.buildRedirectUri;

@RestController
@Tag(name = "Short links", description = "Operations about short links")
@RequiredArgsConstructor
public class ShortLinkController {

    private final ShortLinkService service;

    @PostMapping("/short-links")
    @ApiResponse(responseCode = "201", description = "Short link created",
            headers = @Header(name = "Location", description = "Short link url",
                    schema = @Schema(type = "string", format = "uri"), example = "https://api.example.com/r/D6xg1F"))
    @ApiResponse(responseCode = "400", description = "Invalid url")
    @ApiResponse(responseCode = "429", description = "Too many requests")
    @Operation(summary = "Create short link", parameters = {
            @Parameter(name = "Accept-Language", in = ParameterIn.HEADER,
                    description = "Preferred response language for localized error messages", example = "ru-RU")
    }, requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
            description = "Original URL to shorten",
            content = @Content(schema = @Schema(implementation = CreateShortLinkRequest.class),
                    examples = @ExampleObject(value = "{\"originalUrl\":\"https://google.com\"}"))))
    public ResponseEntity<ShortLinkResponse> createShortUrl(
            @RequestBody @Valid CreateShortLinkRequest request
    ) {
        ShortLinkResponse response = service.create(request);

        URI location = buildRedirectUri("/r/{code}", response.code());
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/r/{shortCode}")
    @Operation(summary = "Redirect by short code", parameters = {
            @Parameter(name = "Accept-Language", in = ParameterIn.HEADER,
                    description = "Preferred response language for localized error messages", example = "ru-RU")
    }, description = "Redirect to original url")
    @ApiResponse(responseCode = "302", description = "Redirect to original url",
            headers = @Header(name = "Location", description = "Original url",
                    schema = @Schema(type = "string", format = "uri"), example = "https://google.com"))
    @ApiResponse(responseCode = "404", description = "Code not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(name = "NotFound", value = """
                            {
                              "timestamp": "2025-12-28T12:15:30.123+03:00",
                              "status": 404,
                              "error": "Not Found",
                              "message": "Code not found",
                              "path": "/r/invalidCode"
                            }
                            """)))
    @ApiResponse(responseCode = "429", description = "Too many requests",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(name = "TooManyRequests", value = """
                            {
                              "timestamp": "2025-12-28T12:15:30.123+03:00",
                              "status": 429,
                              "error": "Too Many Request",
                              "message": "Rate limit exceeded",
                              "path": "/r/D7xlF1"
                            }
                            """)))
    public ResponseEntity<Void> redirect(
            @PathVariable @Parameter(description = "Short code", example = "D7xlF1") String shortCode
    ) {
        ShortLinkResponse response = service.resolve(shortCode);
        return ResponseEntity.status(HttpStatus.FOUND).location(buildOriginalUri(response.originalUrl())).build();
    }
}
