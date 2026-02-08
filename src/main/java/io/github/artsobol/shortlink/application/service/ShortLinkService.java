package io.github.artsobol.shortlink.application.service;

import io.github.artsobol.shortlink.api.dto.CreateShortLinkRequest;
import io.github.artsobol.shortlink.api.dto.ShortLinkResponse;

public interface ShortLinkService {

    ShortLinkResponse create(CreateShortLinkRequest request);

    ShortLinkResponse resolve(String code);
}
