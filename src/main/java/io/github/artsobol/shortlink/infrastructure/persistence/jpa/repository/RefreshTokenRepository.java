package io.github.artsobol.shortlink.infrastructure.persistence.jpa.repository;

import io.github.artsobol.shortlink.infrastructure.persistence.jpa.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByTokenHash(String token);
}
