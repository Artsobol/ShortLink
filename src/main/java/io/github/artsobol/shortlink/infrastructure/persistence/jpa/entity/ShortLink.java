package io.github.artsobol.shortlink.infrastructure.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@Builder
@Table(name = "short_url")
@NoArgsConstructor
@AllArgsConstructor
public class ShortLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "original_url", nullable = false, unique = true)
    private String originalUrl;

    @NotBlank
    @Column(nullable = false, unique = true, length = 6)
    private String code;

    @CreationTimestamp
    @Column(nullable = false, name = "created_at", updatable = false)
    private OffsetDateTime createdAt;
}
