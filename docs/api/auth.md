# Неавторизованные запросы

## 1.1. Зарегистрировать нового пользователя 

## POST
#####  /api/v1/auth/signUp
### Request
##### body:

```json

{
  "username": "User1",
  "password": "userpassword1",
  "email": "user1@example.com",
  "company" : "company A"
}
```
**валидация**

nickname >= 4 символа, <= 15 символов, разрешенные символы a-zA-Z0-9а-яА-Я._-<br/>
password >= 8 символов, <= 100 символов, разрешенные символы a-zA-Z0-9а-яА-Я.,:; _?!+=/'\"*(){}[]-

### Response

### Success:
##### status 200
##### body:

```json
{
  "accessToken": "d4a76068f5104f26975499d22bcd11cc1665995491673"
}
```

### Error:
##### status 400
##### body:

```json
{
  "code": "BAD_CREDENTIALS",
  "message": "Incorrect credentials",
  "errors": {
    "password": "Password contains prohibited characters"
  }
}
```

## 1.2. Авторизация пользователя в системе

## POST
#####  /api/v1/auth/signIn
### Request
##### body:

```json

{
  "username": "user1",
  "password": "userpassword1"
}
```

### Response

### Success:
##### status 200
##### body:

```json
{
  "accessToken": "d4a76068f5104f26975499d22bcd11cc1665995491673"
}
```

### Error:
##### status 400
##### body:

```json
{
  "code": "BAD_CREDENTIALS",
  "message": "Bad credentials"
}
```