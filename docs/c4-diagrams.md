# C4 Architecture - ShortLink

## 1. Context (C1)

![Context](./static/c4-context.png)

**Описание:**

- Пользователь отправляет запросы к сервису сокращения ссылок
- Сервис хранит данные в базе данных PostgreSQL
- Основные взаимодействия: POST /short-links, GET /{shortCode}

---

## 2. Containers (C2)

![Containers](./static/c4-container.png)

**Описание:**

- `URL Shortening Backend` - Spring Boot приложение, обрабатывающее HTTP запросы
- `Database` - PostgreSQL, хранит URL маппинги
- Взаимодействие осуществляется через JPA/Hibernate

---

## 3. Components (C3)

![Components](./static/c4-component.png)

**Описание:**

- `ShortLinkController` принимает входящие HTTP запросы
- `ShortLinkService` реализует бизнес-логику
- `ShortCodeGenerator` создаёт короткие коды
- `ShortLinkRepository` взаимодействует с БД

---

## 4. Classes (UML)

![Class Diagram](./static/c4-class.png)

**Описание:**

- `ShortLink` - сущность, представляющая оригинальный URL и короткий код.
- `ShortLinkController` - REST-контроллер, принимающий HTTP-запросы.
- `ShortLinkService` - бизнес-логика (создание и разрешение ссылок).
- `ShortCodeGenerator` - компонент для генерации коротких кодов.
- `ShortLinkRepository` - слой доступа к базе данных через Spring Data JPA.

## Key Relationships

- Controller → Service
- Service → Repository
- Service → Generator
- Repository → Entity
