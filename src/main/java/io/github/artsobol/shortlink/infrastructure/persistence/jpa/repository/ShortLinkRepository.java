package io.github.artsobol.shortlink.infrastructure.persistence.jpa.repository;

import io.github.artsobol.shortlink.infrastructure.persistence.jpa.entity.ShortLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShortLinkRepository extends JpaRepository<ShortLink, Long> {

    Optional<ShortLink> findByOriginalUrl(String originalUrl);

    Optional<ShortLink> findByCode(String code);
}
