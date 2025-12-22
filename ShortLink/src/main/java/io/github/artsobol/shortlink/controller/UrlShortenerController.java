package io.github.artsobol.shortlink.controller;

import io.github.artsobol.shortlink.entity.dto.RequestOriginalUrl;
import io.github.artsobol.shortlink.entity.dto.ResponseShortUrl;
import io.github.artsobol.shortlink.service.api.UrlShortenerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UrlShortenerController {

    private final UrlShortenerService service;

    @PostMapping("/short-links")
    public ResponseEntity<ResponseShortUrl> createShortUrl(@RequestBody RequestOriginalUrl request) {
        ResponseShortUrl response = service.createShortUrl(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{shortCode}")
    public RedirectView getShortUrl(@PathVariable String shortCode) {
        ResponseShortUrl response = service.getOriginalUrl(shortCode);
        return new RedirectView(response.originalUrl());
    }
}
