package io.github.artsobol.shortlink.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@Builder
@Table(name = "short_url")
@NoArgsConstructor
@AllArgsConstructor
public class ShortUrl {

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
