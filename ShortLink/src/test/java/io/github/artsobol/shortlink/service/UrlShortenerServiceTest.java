package io.github.artsobol.shortlink.service;

import io.github.artsobol.shortlink.entity.ShortUrl;
import io.github.artsobol.shortlink.entity.dto.RequestOriginalUrl;
import io.github.artsobol.shortlink.entity.dto.ResponseShortUrl;
import io.github.artsobol.shortlink.exception.CodeExistsException;
import io.github.artsobol.shortlink.mapper.UrlShortenerMapper;
import io.github.artsobol.shortlink.repository.UrlShortenerRepository;
import io.github.artsobol.shortlink.service.impl.UrlShortenerServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UrlShortenerServiceTest {

    @Mock private UrlShortenerRepository urlRepository;

    @Mock private UrlShortenerMapper urlShortenerMapper;

    @InjectMocks private UrlShortenerServiceImpl urlShortenerService;

    @Test
    @DisplayName("create new short url")
    public void createNewShortUrl() {
        // given
        String originalUrl = "https://www.google.com";
        String code = "D3kX9a";
        ShortUrl shortUrl = ShortUrl.builder().originalUrl(originalUrl).code(code).build();

        // when
        when(urlRepository.findByOriginalUrl(originalUrl)).thenReturn(Optional.empty());
        when(urlRepository.existsByCode(anyString())).thenReturn(false);
        when(urlRepository.save(any(ShortUrl.class))).thenReturn(shortUrl);
        when(urlShortenerMapper.toDto(any(ShortUrl.class))).thenAnswer(inv -> {
            ShortUrl s = inv.getArgument(0);
            return new ResponseShortUrl(s.getOriginalUrl(), s.getCode());
        });

        // then
        ResponseShortUrl response = urlShortenerService.createShortUrl(new RequestOriginalUrl(originalUrl));

        assertEquals(originalUrl, response.originalUrl());
        verify(urlRepository).findByOriginalUrl(originalUrl);
        verify(urlRepository).save(any(ShortUrl.class));
        verify(urlRepository, atLeastOnce()).existsByCode(anyString());
    }

    @Test
    @DisplayName("get originalUrl by code")
    public void getOriginalUrlByCode() {
        // given
        String originalUrl = "https://www.google.com";
        String code = "D3kX9a";
        ShortUrl shortUrl = ShortUrl.builder().originalUrl(originalUrl).code(code).build();

        // when
        when(urlRepository.findByCode(code)).thenReturn(Optional.of(shortUrl));
        when(urlShortenerMapper.toDto(any(ShortUrl.class))).thenReturn(new ResponseShortUrl(originalUrl, code));

        // then
        ResponseShortUrl response = urlShortenerService.getOriginalUrl(code);

        assertEquals(originalUrl, response.originalUrl());
        assertEquals(code, response.code());
        verify(urlRepository).findByCode(code);
        verify(urlShortenerMapper).toDto(any(ShortUrl.class));
    }

    @Test
    @DisplayName("throw CodeExistsException when code not found")
    public void throwCodeExistsExceptionWhenCodeNotFound() {
        // given
        String code = "invalidCode";

        // when
        when(urlRepository.findByCode(code)).thenReturn(Optional.empty());

        // then
        assertThrows(CodeExistsException.class, () -> urlShortenerService.getOriginalUrl(code));
    }
}
