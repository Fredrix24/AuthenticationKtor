# Ktor CRUD API с JWT-аутентификацией

Backend-приложение на Kotlin/Ktor: CRUD-операции для сущности **Task** и система аутентификации пользователей с JWT.

## 🚀 Запуск

```bash
./gradlew run        # Linux / macOS
gradlew.bat run      # Windows
```

Сервер: `http://localhost:8080`
Документация: `http://localhost:8080/openapi`

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

## 📚 Swagger UI

Документация API доступна по адресу: **http://localhost:8080/openapi**

Генерируется автоматически из KDoc-комментариев на маршрутах — если измените маршрут, документация обновится.

## 🧪 Тесты

```bash
./gradlew test
```

8 тестов покрывают:

- `TaskRoutesTest` — публичный доступ к задачам, 404/400/401
- `AuthRoutesTest` — регистрация, дубликат логина, вход, неверный пароль

Все тесты проходят: **8/8**.

## 🛡️ Обработка ошибок

Централизованная обработка через `StatusPages`:

- Любое необработанное исключение → `500 Internal Server Error` с сообщением
- Несуществующий маршрут → `404 Not Found`
- Отсутствующий/невалидный JWT → `401 Unauthorized`

## 📝 Логирование

`CallLogging` middleware логирует все входящие запросы в формате:

```
[GET] /tasks -> 200 | UA: ktor-client
[POST] /auth/register -> 201 | UA: curl/8.0
[POST] /tasks -> 401 | UA: PostmanRuntime/7.32.0
```

Запросы к `/openapi` и `/swagger` исключены из логирования.

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


## 🔒 Безопасность

- Пароли хэшируются через **BCrypt** с cost=12 — открытый пароль нигде не хранится.
- При входе используется `BCrypt.checkpw()` — сверка хэша, а не прямое сравнение строк.
- JWT подписывается алгоритмом **HMAC256**, содержит claim `userId` и `exp` (1 час).
- Защищённые маршруты требуют заголовок `Authorization: Bearer <token>`.
