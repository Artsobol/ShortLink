package io.github.artsobol.shortlink.shortlink.service;

import io.github.artsobol.shortlink.exception.domain.CodeGenerationException;
import io.github.artsobol.shortlink.exception.domain.CodeNotFoundException;
import io.github.artsobol.shortlink.shortlink.dto.CreateShortLinkRequest;
import io.github.artsobol.shortlink.shortlink.dto.ShortLinkResponse;
import io.github.artsobol.shortlink.shortlink.entity.ShortLink;
import io.github.artsobol.shortlink.shortlink.repository.ShortLinkRepository;
import io.github.artsobol.shortlink.shortlink.utils.GenerateCodeUtils;
import io.github.artsobol.shortlink.user.mapper.ShortLinkMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShortLinkServiceImpl implements ShortLinkService {

    private static final int MAX_RETRIES = 5;

    private final ShortLinkRepository repository;
    private final ShortLinkMapper mapper;

    @Override
    @Transactional
    @CachePut(cacheNames = "shortLink", key = "#result.code()", unless = "#result == null")
    public ShortLinkResponse create(CreateShortLinkRequest request) {
        String originalUrl = request.originalUrl();

        log.debug("Creating shortLink for originalUrl={}", originalUrl);

        return repository.findByOriginalUrl(originalUrl).map(mapper::toDto).orElseGet(() -> {
            try {
                ShortLinkResponse response = mapper.toDto(saveCode(originalUrl));
                log.info("ShortLink created for originalUrl={} with code={}", originalUrl, response.code());
                return response;
            } catch (DataIntegrityViolationException e) {
                log.debug("Cannot create shortLink for originalUrl={}", originalUrl);
                return repository.findByOriginalUrl(originalUrl).map(mapper::toDto).orElseThrow(() -> e);
            }
        });
    }

    @Override
    @Cacheable(cacheNames = "shortLink", key = "#code")
    public ShortLinkResponse resolve(String code) {
        return repository.findByCode(code).map(mapper::toDto).orElseThrow(() -> new CodeNotFoundException(code));
    }

    private ShortLink saveCode(String originalUrl) {
        for (int retry = 0; retry < MAX_RETRIES; retry++) {
            String code = GenerateCodeUtils.generateCode();
            ShortLink shortLink = ShortLink.builder().code(code).originalUrl(originalUrl).build();
            try {
                log.debug("Saving shortLink (code={}, originalUrl={})", code, originalUrl);
                return repository.save(shortLink);
            } catch (DataIntegrityViolationException e) {
                log.debug(
                        "Collision detected when saving shortLink (code={}, originalUrl={}, attempt={}/{})",
                        shortLink.getCode(),
                        originalUrl,
                        retry + 1,
                        MAX_RETRIES
                );
            }
        }
        throw new CodeGenerationException(MAX_RETRIES);
    }
}
