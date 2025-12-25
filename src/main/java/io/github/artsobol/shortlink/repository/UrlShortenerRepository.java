package io.github.artsobol.shortlink.repository;

import io.github.artsobol.shortlink.entity.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlShortenerRepository extends JpaRepository<ShortUrl, Long> {

    Optional<ShortUrl> findByOriginalUrl(String originalUrl);

    Optional<ShortUrl> findByCode(String code);

    boolean existsByCode(String code);
}
