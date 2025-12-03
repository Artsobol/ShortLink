# ShortLink - API

## Post

`POST /short-links`

**Request:**

```json
{
  "url": "https://your_url.com"
}
```

**Success: 201 Created**

```json
{
  "shortCode": "abc123",
  "shortUrl": "https://short.link/abc123"
}
```

Поведение:

- Если URL-адрес был ранее сокращен, сервис возвращает существующую короткую ссылку (идемпотентное поведение)

**Error: 400 Bad Request:**

```json
{
  "error": "Invalid URL"
}
```

## GET

`GET /{shortCode}`

**Success: 302 Found**

Headers:

```http
Location: https://original-url.com
```

**Error: 404 Not Found**

```json
{
  "error": "Short code not found"
}
```
