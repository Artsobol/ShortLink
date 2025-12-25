package io.github.artsobol.shortlink.controller;

import io.github.artsobol.shortlink.entity.dto.RequestOriginalUrl;
import io.github.artsobol.shortlink.entity.dto.ResponseShortUrl;
import io.github.artsobol.shortlink.service.api.UrlShortenerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URI;

import static io.github.artsobol.shortlink.utils.CreateUriUtils.buildRedirectUri;

@RestController
@Tag(name = "Short link", description = "Operations about short links")
@RequiredArgsConstructor
public class UrlShortenerController {

    private final UrlShortenerService service;

    @PostMapping("/short-links")
    @Operation(summary = "Create short link", description = "Create short link for given url")
    public ResponseEntity<ResponseShortUrl> createShortUrl(@RequestBody @Valid RequestOriginalUrl request) {
        ResponseShortUrl response = service.createShortUrl(request);

        URI location = buildRedirectUri("/r/{code}", response.code());
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/r/{shortCode}")
    @Operation(summary = "Redirect by short code", description = "Redirect to original url")
    @ApiResponses({
            @ApiResponse(responseCode = "302", description = "Redirect to original url", headers=@Header(name="Location", description="Original url")),
            @ApiResponse(responseCode = "404", description = "Code not found"),
            @ApiResponse(responseCode = "429", description = "Too many requests")
    })
    public RedirectView getShortUrl(
            @Parameter(description = "Short code", example = "abc123") @PathVariable String shortCode) {
        ResponseShortUrl response = service.getOriginalUrl(shortCode);
        RedirectView view = new RedirectView(response.originalUrl());
        view.setStatusCode(HttpStatus.FOUND);
        return view;
    }
}
