package io.github.artsobol.shortlink.shortlink.service;

import io.github.artsobol.shortlink.shortlink.dto.CreateShortLinkRequest;
import io.github.artsobol.shortlink.shortlink.dto.ShortLinkResponse;

public interface ShortLinkService {

    ShortLinkResponse create(CreateShortLinkRequest request);

    ShortLinkResponse resolve(String code);
}
