package io.github.artsobol.shortlink.repository;

import io.github.artsobol.shortlink.shortlink.entity.ShortLink;
import io.github.artsobol.shortlink.shortlink.repository.ShortLinkRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.liquibase.autoconfigure.LiquibaseAutoConfiguration;
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
@ImportAutoConfiguration(LiquibaseAutoConfiguration.class)
class ShortLinkRepositoryTest {

    @Container
    @SuppressWarnings("resource")
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:18-alpine").withDatabaseName("test_db").withUsername("test_user").withPassword("test_password");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.liquibase.enabled", () -> true);
    }

    @Autowired private ShortLinkRepository repository;

    @Test
    @DisplayName("findByOriginalUrl returns entity when url exists")
    void findByOriginalUrl_returnsEntity_whenUrlExists() {
        // given
        String originalUrl = "https://www.google.com";
        String code = "D3kX9a";
        repository.save(ShortLink.builder().originalUrl(originalUrl).code(code).build());

        // when
        Optional<ShortLink> result = repository.findByOriginalUrl(originalUrl);

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
        repository.save(ShortLink.builder().originalUrl(originalUrl).code(code).build());

        // when
        Optional<ShortLink> result = repository.findByCode(code);

        // then
        assertThat(result).isPresent().hasValueSatisfying(shortUrl -> {
            assertThat(shortUrl.getOriginalUrl()).isEqualTo(originalUrl);
            assertThat(shortUrl.getCode()).isEqualTo(code);
        });
    }

    @Test
    @DisplayName("codeShouldBeUnique throw exception when code exists")
    void codeShouldBeUnique() {
        repository.saveAndFlush(ShortLink.builder().originalUrl("https://a.com").code("dup").build());

        assertThatThrownBy(() -> repository.saveAndFlush(ShortLink.builder()
                .originalUrl("https://b.com")
                .code("dup")
                .build())).isInstanceOf(DataIntegrityViolationException.class)
                .hasRootCauseInstanceOf(org.postgresql.util.PSQLException.class);
    }

    @Test
    @DisplayName("urlShouldBeUnique throw exception when url exists")
    void urlShouldBeUnique() {
        repository.saveAndFlush(ShortLink.builder().originalUrl("https://a.com").code("Ffd2d").build());

        assertThatThrownBy(() -> repository.saveAndFlush(ShortLink.builder()
                .originalUrl("https://a.com")
                .code("Fasd32")
                .build())).isInstanceOf(DataIntegrityViolationException.class)
                .hasRootCauseInstanceOf(org.postgresql.util.PSQLException.class);
    }
}
