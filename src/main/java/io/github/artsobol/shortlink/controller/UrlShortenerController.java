package io.github.artsobol.shortlink.controller;

import io.github.artsobol.shortlink.entity.dto.RequestOriginalUrl;
import io.github.artsobol.shortlink.entity.dto.ResponseShortUrl;
import io.github.artsobol.shortlink.exception.ErrorResponse;
import io.github.artsobol.shortlink.service.api.UrlShortenerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static io.github.artsobol.shortlink.utils.CreateUriUtils.buildOriginalUri;
import static io.github.artsobol.shortlink.utils.CreateUriUtils.buildRedirectUri;

@RestController
@Tag(name = "Short links", description = "Operations about short links")
@RequiredArgsConstructor
public class UrlShortenerController {

    private final UrlShortenerService service;

    @PostMapping("/short-links")
    @ApiResponses({
            @ApiResponse(responseCode = "201",
                         description = "Short link created",
                         headers = @Header(name = "Location",
                                           description = "Short link url",
                                           schema = @Schema(type = "string", format = "uri"),
                                           example = "https://api.example.com/r/D6xg1F")),
            @ApiResponse(responseCode = "400", description = "Invalid url"),
            @ApiResponse(responseCode = "429", description = "Too many requests")
    })
    @Operation(summary = "Create short link",
               requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
                                                                                   description = "Original URL to shorten",
                                                                                   content = @Content(schema = @Schema(
                                                                                           implementation = RequestOriginalUrl.class),
                                                                                                      examples = @ExampleObject(
                                                                                                              value = "{\"originalUrl\":\"https://google.com\"}"))))
    public ResponseEntity<ResponseShortUrl> createShortUrl(@RequestBody @Valid RequestOriginalUrl request) {
        ResponseShortUrl response = service.createShortUrl(request);

        URI location = buildRedirectUri("/r/{code}", response.code());
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/r/{shortCode}")
    @Operation(summary = "Redirect by short code", description = "Redirect to original url")
    @ApiResponses({
            @ApiResponse(responseCode = "302",
                         description = "Redirect to original url",
                         headers = @Header(name = "Location",
                                           description = "Original url",
                                           schema = @Schema(type = "string", format = "uri"),
                                           example = "https://google.com")),
            @ApiResponse(responseCode = "404",
                         description = "Code not found",
                         content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponse.class),
                                            examples = @ExampleObject(name = "NotFound", value = """
                                                                                                 {
                                                                                                    "timestamp": "2025-12-28T12:15:30.123+03:00",
                                                                                                      "status": 404,
                                                                                                      "error": "Not Found",
                                                                                                      "message": "Code not found",
                                                                                                      "path": "/r/invalidCode"
                                                                                                 }
                                                                                                 """))),
            @ApiResponse(responseCode = "429",
                         description = "Too many requests",
                         content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponse.class),
                                            examples = @ExampleObject(name = "TooManyRequests", value = """
                                                                                                        {
                                                                                                         "timestamp": "2025-12-28T12:15:30.123+03:00",
                                                                                                         "status": 429,
                                                                                                         "error": "Too Many Request",
                                                                                                         "message": "Rate limit exceeded",
                                                                                                         "path": "/r/D7xlF1"
                                                                                                        }
                                                                                                        """)))
    })
    public ResponseEntity<Void> redirect(
            @Parameter(description = "Short code", example = "D7xlF1") @PathVariable("shortCode") String shortCode) {
        ResponseShortUrl response = service.getOriginalUrl(shortCode);
        return ResponseEntity.status(HttpStatus.FOUND).location(buildOriginalUri(response.originalUrl())).build();
    }
}
