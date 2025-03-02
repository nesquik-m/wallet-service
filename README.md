# Wallet API: RESTful Service for Managing Wallets

## Описание

Этот проект представляет собой RESTful API для управления кошельками.
Он позволяет пополнять и списывать средства со счета кошелька, а также получать текущий баланс.  
API разработан для работы в условиях высокой нагрузки и обеспечивает консистентность данных при большом количестве одновременных запросов.

## Технологии

- **Язык:** Java 17
- **Фреймворк:** Spring Boot 3
- **База данных:** PostgreSQL 
- **Миграции:** Liquibase
- **Контейнеризация:** Docker
- **Оркестрация:** Docker Compose
- **Тестирование:** JUnit, Mockito
- **Сборка:** Maven

## API Endpoints

### **POST `/api/v1/wallet`:** Создание/изменение кошелька (пополнение/снятие)

**Request Body:**

json
```
{
"walletId": "UUID",
"operationType": "DEPOSIT" или "WITHDRAW",
"amount": 1000
}
```
**Response:**

200 OK: Успешное выполнение операции. 
```
{
  "walletId": "UUID",
  "amount": 1000,
  "operationType": "DEPOSIT",
  "transactionId": 1
}
```
400 Bad Request: Невалидный JSON, неверный operationType, отрицательная сумма. Сообщение об ошибке в теле ответа. 
```
{
  "error": {
    "code": "INVALID_DATA",
    "message": "amount cannot be null; operationType cannot be null"
  }
}
```
402 Payment Required: Недостаточно средств на счете для операции WITHDRAW. Сообщение об ошибке в теле ответа.
```
{
  "error": {
    "code": "NOT_ENOUGH_BALANCE",
    "message": "Not enough balance in wallet with ID [UUID] to withdraw amount: [amount]"
  }
}
```

### GET /api/v1/wallet/{WALLET_UUID}: Получение баланса кошелька
**Responses:**

200 OK: Успешное получение баланса.
```
json
    {
     "walletId": "UUID",
     "balance": 5000
    }
```

404 Not Found: Кошелек с указанным WALLET_UUID не найден. Сообщение об ошибке в теле ответа.

```
{
  "error": {
    "code": "OBJECT_NOT_FOUND",
    "message": "Wallet with id [UUID] not found"
  }
}
```
## Обработка ошибок:

- Для всех ошибок, возвращаемых API, предусмотрен структурированный формат ответа, включающий код ошибки и сообщение. Это упрощает отладку и обработку ошибок на стороне клиента. 
- Используются стандартные HTTP-коды ошибок (400, 402, 404) для обозначения различных типов проблем.
- Предусмотрена валидация входящих данных (JSON schema validation) для предотвращения обработки некорректных запросов.

## Настройка переменных окружения:

1. Создайте файл .env в корне проекта. 
2. Добавьте необходимые переменные окружения:

    ```
    DATABASE_HOST=
    DATABASE_PORT=
    DATABASE_NAME=
    DATABASE_SCHEMA=
    DATABASE_CONNECTION_TIMEOUT=
    POSTGRES_USER=
    POSTGRES_PASSWORD=
    ```
3. Соберите приложение с помощью Maven:

```
bash
./mvnw clean install
```

4. Запустите приложение с помощью Docker Compose:

```
bash
docker-compose up --build
```

## Пример запросов
bash
```
# Получение баланса кошелька
curl http://localhost:8080/api/v1/wallet/a1b2c3d4-e5f6-7890-1234-567890abcdef
```
```
# Пополнение кошелька
curl -X POST -H "Content-Type: application/json" -d '{
 "walletId": "a1b2c3d4-e5f6-7890-1234-567890abcdef",
 "operationType": "DEPOSIT",
 "amount": 1000
}' http://localhost:8080/api/v1/wallet
```
```
# Снятие средств с кошелька
curl -X POST -H "Content-Type: application/json" -d '{
 "walletId": "a1b2c3d4-e5f6-7890-1234-567890abcdef",
 "operationType": "WITHDRAW",
 "amount": 500
}' http://localhost:8080/api/v1/wallet
```