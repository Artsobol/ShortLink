# ShortLink

ShortLink - REST-сервис для сокращения URL с поддержкой JWT-аутентификации, кэширования, локализации и контейнерного
запуска.

## Стек технологий

- **Java 21**
- **Spring Boot 4.0.2** (Spring Security, Data JPA, Redis, Actuator)
- **PostgreSQL** (хранение ссылок и пользователей)
- **Redis** (кэширование и Refresh-токены)
- **Liquibase** (управление миграциями БД)
- **Docker & Docker Compose**
- **MapStruct & Lombok**
- **JJWT** (JSON Web Token)

## Основные возможности

1. **Сокращение URL:** получение короткого кода для длинных ссылок.
2. **Идемпотентность:** при повторном запросе одной и той же ссылки возвращается уже существующий код.
3. **Безопасность:**
    - Регистрация и аутентификация пользователей.
    - JWT-защита (Access & Refresh tokens).
    - Refresh-токен безопасно хранится в HttpOnly Cookie.
4. **Кэширование:** использование Redis для быстрого доступа к популярным ссылкам (политика LFU).
5. **Интернационализация (i18n):** поддержка ответов на русском и английском языках (через заголовок `Accept-Language`).
6. **Аудит:** автоматическое отслеживание автора и времени создания каждой ссылки.
7. **Трассировка:** логирование с использованием MDC для отслеживания запросов.

## Архитектура

Проект построен по многослойной архитектуре (Layered Architecture):

- `api`: контроллеры, DTO и обработка исключений.
- `application`: бизнес-логика и сервисы.
- `domain`: доменные модели и сущности.
- `infrastructure`: конфигурация, персистентность, мапперы и утилиты.

## Запуск

1. Склонируйте репозиторий.
2. Создайте файл `.env` на основе [.env.example](.env.example).
3. Запустите проект через Docker Compose:

```bash
docker compose --env-file .env.local \
   -f docker-compose.yml \
   -f docker-compose.local.yml \
   up --build
```

Сервис будет доступен по адресу: `http://localhost:8080`

## Конфигурация Redis

Настройки кэширования находятся в `infra/redis/cache/redis.conf`:

```redis.conf
maxmemory 512mb
maxmemory-policy allkeys-lfu
appendonly no
```

## Документация

- [API документация (Markdown)](/docs/API.md)
- [Swagger UI](http://localhost:8080/swagger-ui.html) (после запуска)
- [C4 диаграммы](/docs/c4-diagrams.md)
