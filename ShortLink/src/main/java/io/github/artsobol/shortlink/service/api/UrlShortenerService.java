package io.github.artsobol.shortlink.service.api;

import io.github.artsobol.shortlink.entity.dto.RequestOriginalUrl;
import io.github.artsobol.shortlink.entity.dto.ResponseShortUrl;

public interface UrlShortenerService {

    ResponseShortUrl createShortUrl(RequestOriginalUrl request);

    ResponseShortUrl getOriginalUrl(String code);
}
