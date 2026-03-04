# Changelog

## [1.0.0] - 2026-01-07

### Added

- Initial stable release

## [1.1.0] - 2026-02-08

### Added

- **Security and Authentication:**
    - Implemented JWT-based authentication (Access and Refresh tokens).
    - Added user registration and login functionality.
    - Configured Spring Security with JWT processing filters.
    - Introduced `User`, `Role`, and `RefreshToken` entities.
    - Implemented `AuditorAware` for automatic tracking of entity creator and last modifier.
- **Infrastructure:**
    - Configured caching using Redis.
    - Added internationalization (i18n) support for error and validation messages (RU/EN).
    - Improved logging: added `MdcFilter` for request tracing and configured Logback profiles.
- **Database:**
    - Added Liquibase changelogs for creating user and token tables.

### Changed

- **Spring Boot 4 Upgrade:** Updated project to Spring Boot 4.0.2.
- **Architecture Refactoring:**
    - Reorganized the project into a clearer layered structure:
        - `api`: controllers and DTOs.
        - `application`: services and business logic.
        - `domain`: domain models.
        - `infrastructure`: configuration, persistence (JPA), mappers, and utilities.
- **API:**
    - Updated Swagger/OpenAPI documentation.
    - Improved exception handling in `CommonControllerAdvice`.
- **Configuration:**
    - Renamed the `dev` profile to `local`.
    - Updated Docker Compose configuration to support Redis.

### Fixed

- Fixed `CacheConfig` to be compatible with Spring Boot 4/Jackson 3 by injecting `ObjectMapper` into
  `GenericJacksonJsonRedisSerializer`.
- Updated and fixed existing repository and service tests to align with the new data structure.

## [1.1.1] - 2026-03-04

### Changed

- Updated project dependencies to newer patch versions.
- **Security Configuration:**
    - Removed `@Component` from `JwtTokenProvider` and `JwtAuthenticationFilter`.
    - Registered `JwtTokenProvider` and `JwtAuthenticationFilter` explicitly in `SecurityConfig`.
- **Architecture Refactoring:**
    - Reorganized package structure for clearer module boundaries (`api`, `infrastructure`, `exception`, feature
      modules).

### Fixed

- **Tests:**
    - Updated repository tests to run Liquibase migrations when using Testcontainers.