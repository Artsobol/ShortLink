package io.github.artsobol.shortlink.service.api;

import io.github.artsobol.shortlink.entity.dto.CreateShortLinkRequest;
import io.github.artsobol.shortlink.entity.dto.ShortLinkResponse;

public interface ShortLinkService {

    ShortLinkResponse create(CreateShortLinkRequest request);

    ShortLinkResponse resolve(String code);
}
