package io.github.artsobol.shortlink.repository;

import io.github.artsobol.shortlink.entity.ShortUrl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UrlShortenerRepositoryTest {

    @Container @SuppressWarnings("resource") static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:16-alpine").withDatabaseName("test_db").withUsername("test_user").withPassword("test_password");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        registry.add("spring.liquibase.enabled", () -> true);
    }

    @Autowired private UrlShortenerRepository repository;

    @Test
    @DisplayName("findByOriginalUrl returns entity when url exists")
    void findByOriginalUrl_returnsEntity_whenUrlExists() {
        // given
        String originalUrl = "https://www.google.com";
        String code = "D3kX9a";
        repository.save(ShortUrl.builder().originalUrl(originalUrl).code(code).build());

        // when
        Optional<ShortUrl> result = repository.findByOriginalUrl(originalUrl);

        // then
        assertThat(result).isPresent().hasValueSatisfying(shortUrl -> {
            assertThat(shortUrl.getOriginalUrl()).isEqualTo(originalUrl);
            assertThat(shortUrl.getCode()).isEqualTo(code);
        });
    }

    @Test
    @DisplayName("findByCode returns entity when code exists")
    void findByCode_returnsEntity_whenCodeExists() {
        // given
        String originalUrl = "https://www.google.com";
        String code = "D3kX9a";
        repository.save(ShortUrl.builder().originalUrl(originalUrl).code(code).build());

        // when
        Optional<ShortUrl> result = repository.findByCode(code);

        // then
        assertThat(result).isPresent().hasValueSatisfying(shortUrl -> {
            assertThat(shortUrl.getOriginalUrl()).isEqualTo(originalUrl);
            assertThat(shortUrl.getCode()).isEqualTo(code);
        });
    }

    @Test
    @DisplayName("codeShouldBeUnique throw exception when code exists")
    void codeShouldBeUnique() {
        repository.saveAndFlush(ShortUrl.builder().originalUrl("https://a.com").code("dup").build());

        assertThatThrownBy(() -> repository.saveAndFlush(ShortUrl.builder()
                                                                 .originalUrl("https://b.com")
                                                                 .code("dup")
                                                                 .build())).isInstanceOf(DataIntegrityViolationException.class)
                                                                           .hasRootCauseInstanceOf(org.postgresql.util.PSQLException.class);
    }

    @Test
    @DisplayName("urlShouldBeUnique throw exception when url exists")
    void urlShouldBeUnique() {
        repository.saveAndFlush(ShortUrl.builder().originalUrl("https://a.com").code("Ffd2d").build());

        assertThatThrownBy(() -> repository.saveAndFlush(ShortUrl.builder()
                                                                 .originalUrl("https://a.com")
                                                                 .code("Fasd32")
                                                                 .build())).isInstanceOf(DataIntegrityViolationException.class)
                                                                           .hasRootCauseInstanceOf(org.postgresql.util.PSQLException.class);
    }
}
