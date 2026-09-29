# Ktor CRUD API с JWT-аутентификацией

Backend-приложение на Kotlin/Ktor: CRUD-операции для сущности **Task** и система аутентификации пользователей с JWT.

## 🚀 Запуск

```bash
./gradlew run        # Linux / macOS
gradlew.bat run      # Windows
```

Сервер: `http://localhost:8080`

## 📋 Маршруты

| Метод | URL | Доступ | Код успеха |
|-------|-----|--------|------------|
| GET | `/tasks` | Публичный | 200 |
| GET | `/tasks/{id}` | Публичный | 200 |
| POST | `/tasks` | **JWT** | 201 |
| PUT | `/tasks/{id}` | **JWT** | 200 |
| DELETE | `/tasks/{id}` | **JWT** | 204 |
| POST | `/auth/register` | Публичный | 201 |
| POST | `/auth/login` | Публичный | 200 |

## 🔐 Аутентификация

### Регистрация

```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username": "vova", "password": "secret123"}'
```

Ответ: `201 Created`, пароль хранится в виде BCrypt-хэша.

### Вход

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "vova", "password": "secret123"}'
```

Ответ: `200 OK` с полем `token` — JWT сроком действия 1 час, содержит claim `userId`.

### Защищённый запрос

```bash
curl -X POST http://localhost:8080/tasks \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <ВАШ_ТОКЕН>" \
  -d '{"title": "Новая задача", "description": "Описание"}'
```

Без токена — `401 Unauthorized`.

## 📥 Примеры для остальных маршрутов

**GET /tasks**
```bash
curl http://localhost:8080/tasks
```

**GET /tasks/{id}**
```bash
curl http://localhost:8080/tasks/1
```

**PUT /tasks/{id}**
```bash
curl -X PUT http://localhost:8080/tasks/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <ТОКЕН>" \
  -d '{"completed": true}'
```

**DELETE /tasks/{id}**
```bash
curl -X DELETE http://localhost:8080/tasks/1 \
  -H "Authorization: Bearer <ТОКЕН>"
```
Ответ: `204 No Content`.

## ✅ HTTP-статусы

| Код | Когда |
|-----|-------|
| 200 | Успешный GET / PUT |
| 201 | Ресурс создан (POST) |
| 204 | Ресурс удалён (DELETE) |
| 400 | Невалидные данные, некорректный ID |
| 401 | Нет токена / токен недействителен / неверный пароль |
| 404 | Ресурс не найден |
| 409 | Логин уже занят при регистрации |
