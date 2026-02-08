package io.github.artsobol.shortlink.service;

import io.github.artsobol.shortlink.api.dto.CreateShortLinkRequest;
import io.github.artsobol.shortlink.api.dto.ShortLinkResponse;
import io.github.artsobol.shortlink.application.impl.ShortLinkServiceImpl;
import io.github.artsobol.shortlink.exception.domain.shortlink.CodeNotFoundException;
import io.github.artsobol.shortlink.infrastructure.persistence.jpa.entity.ShortLink;
import io.github.artsobol.shortlink.infrastructure.persistence.jpa.repository.ShortLinkRepository;
import io.github.artsobol.shortlink.infrastructure.persistence.mapper.ShortLinkMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShortLinkServiceTest {

    @Mock private ShortLinkRepository urlRepository;

    @Mock private ShortLinkMapper shortLinkMapper;

    @InjectMocks private ShortLinkServiceImpl urlShortenerService;

    @Test
    @DisplayName("create new short url")
    void createNewShortUrl() {
        // given
        String originalUrl = "https://www.google.com";
        String code = "D3kX9a";
        ShortLink shortLink = ShortLink.builder().originalUrl(originalUrl).code(code).build();

        // when
        when(urlRepository.findByOriginalUrl(originalUrl)).thenReturn(Optional.empty());
        when(urlRepository.save(any(ShortLink.class))).thenReturn(shortLink);
        when(shortLinkMapper.toDto(any(ShortLink.class))).thenAnswer(inv -> {
            ShortLink s = inv.getArgument(0);
            return new ShortLinkResponse(s.getOriginalUrl(), s.getCode());
        });

        // then
        ShortLinkResponse response = urlShortenerService.create(new CreateShortLinkRequest(originalUrl));

        assertEquals(originalUrl, response.originalUrl());
        verify(urlRepository).findByOriginalUrl(originalUrl);
        verify(urlRepository).save(any(ShortLink.class));
    }

    @Test
    @DisplayName("get originalUrl by code")
    void resolveByCode() {
        // given
        String originalUrl = "https://www.google.com";
        String code = "D3kX9a";
        ShortLink shortLink = ShortLink.builder().originalUrl(originalUrl).code(code).build();

        // when
        when(urlRepository.findByCode(code)).thenReturn(Optional.of(shortLink));
        when(shortLinkMapper.toDto(any(ShortLink.class))).thenReturn(new ShortLinkResponse(originalUrl, code));

        // then
        ShortLinkResponse response = urlShortenerService.resolve(code);

        assertEquals(originalUrl, response.originalUrl());
        assertEquals(code, response.code());
        verify(urlRepository).findByCode(code);
        verify(shortLinkMapper).toDto(any(ShortLink.class));
    }

    @Test
    @DisplayName("throw CodeExistsException when code not found")
    void throwCodeExistsExceptionWhenCodeNotFound() {
        // given
        String code = "invalidCode";

        // when
        when(urlRepository.findByCode(code)).thenReturn(Optional.empty());

        // then
        assertThrows(CodeNotFoundException.class, () -> urlShortenerService.resolve(code));
    }
}
