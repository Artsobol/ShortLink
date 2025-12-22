package io.github.artsobol.shortlink.service.impl;

import io.github.artsobol.shortlink.entity.ShortUrl;
import io.github.artsobol.shortlink.entity.dto.RequestOriginalUrl;
import io.github.artsobol.shortlink.entity.dto.ResponseShortUrl;
import io.github.artsobol.shortlink.exception.CodeExistsException;
import io.github.artsobol.shortlink.exception.CodeGenerationException;
import io.github.artsobol.shortlink.mapper.UrlShortenerMapper;
import io.github.artsobol.shortlink.repository.UrlShortenerRepository;
import io.github.artsobol.shortlink.service.api.UrlShortenerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static io.github.artsobol.shortlink.utils.GenerateCodeUtils.generateCode;

@Service
@Slf4j
@RequiredArgsConstructor
public class UrlShortenerServiceImpl implements UrlShortenerService {

    private static final int MAX_RETRIES = 5;

    private final UrlShortenerRepository repository;
    private final UrlShortenerMapper mapper;

    @Override
    @Transactional
    public ResponseShortUrl createShortUrl(RequestOriginalUrl request) {
        String originalUrl = request.originalUrl();
        return repository.findByOriginalUrl(originalUrl).map(mapper::toDto).orElseGet(() -> {
            ShortUrl shortUrl = saveShortUrl(originalUrl);
            return mapper.toDto(shortUrl);
        });
    }

    @Override
    public ResponseShortUrl getOriginalUrl(String code) {
        return repository.findByCode(code).map(mapper::toDto).orElseThrow(() -> new CodeExistsException("code not found"));
    }

    private ShortUrl saveShortUrl(String originalUrl) {
        for (int i = 0; i < MAX_RETRIES; i++) {
            ShortUrl url = generateShortUrl(originalUrl);
            try {
                return repository.save(url);
            } catch (DataIntegrityViolationException e) {
                log.debug("Collision on code {} for url {}", url.getCode(), originalUrl, e);
            }
        }
        throw new CodeGenerationException("Failed to generate unique code for " + MAX_RETRIES + " tryings");
    }

    private ShortUrl generateShortUrl(String originalURL) {
        String code;
        do {
            code = generateCode();
        } while (existsByCode(code));
        return ShortUrl.builder().code(code).originalUrl(originalURL).build();
    }

    private boolean existsByCode(String code) {
        return repository.existsByCode(code);
    }
}
