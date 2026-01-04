package io.github.artsobol.shortlink.service.impl;

import io.github.artsobol.shortlink.entity.ShortLink;
import io.github.artsobol.shortlink.entity.dto.CreateShortLinkRequest;
import io.github.artsobol.shortlink.entity.dto.ShortLinkResponse;
import io.github.artsobol.shortlink.exception.CodeGenerationException;
import io.github.artsobol.shortlink.exception.CodeNotFoundException;
import io.github.artsobol.shortlink.mapper.ShortLinkMapper;
import io.github.artsobol.shortlink.repository.ShortLinkRepository;
import io.github.artsobol.shortlink.service.api.ShortLinkService;
import io.github.artsobol.shortlink.utils.GenerateCodeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ShortLinkResponse create(CreateShortLinkRequest request) {
        String originalUrl = request.originalUrl();
        return repository.findByOriginalUrl(originalUrl).map(mapper::toDto).orElseGet(() -> {
            try {
                return mapper.toDto(saveCode(originalUrl));
            } catch (DataIntegrityViolationException e) {
                return repository.findByOriginalUrl(originalUrl).map(mapper::toDto).orElseThrow(() -> e);
            }
        });
    }

    @Override
    public ShortLinkResponse resolve(String code) {
        return repository.findByCode(code).map(mapper::toDto).orElseThrow(() -> new CodeNotFoundException(code));
    }

    private ShortLink saveCode(String originalUrl) {
        for (int i = 0; i < MAX_RETRIES; i++) {
            String code = GenerateCodeUtils.generateCode();
            ShortLink shortLink = ShortLink.builder().code(code).originalUrl(originalUrl).build();
            try {
                return repository.save(shortLink);
            } catch (DataIntegrityViolationException e) {
                log.debug(
                        "Data integrity violation while saving shortLink (code={}, originalUrl={})",
                        shortLink.getCode(),
                        originalUrl,
                        e
                );
            }
        }
        throw new CodeGenerationException(MAX_RETRIES);
    }
}
