## 1.1. Авторизация пользователя в системе

### Request

### POST

##### /api/v1/auth/singIn

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

## 1.2. Получение списка состояний устройств в убежищах

### Request

### GET

##### /api/v1/observer/shelter-list

_**parameters**_

```
- int page (defaultValue = "1")
- int size (defaultValue = "50")
```

### Response

### Success:

##### status 200

##### body:

```json
{
  "content": [
    {
      "alarmed": false,
      "lastHeartbeat": 1742775224,
      "controlPointIdx": 3415
    },
    {
      "alarmed": false,
      "lastHeartbeat": 1742698848,
      "controlPointIdx": 3125
    },
    {
      "alarmed": false,
      "lastHeartbeat": 1742775226,
      "controlPointIdx": 3804
    }
  ],
  "totalPages": 1,
  "totalElements": 3,
  "pageSize": 50,
  "currentPage": 1,
  "first": true,
  "last": false
}
```

## 1.3. Получение состояние тревоги в регионе

### Request

### GET

##### /api/v1/observer/region-state/{regionId}   (Для Одессы - /api/v1/observer/region-state/18)

### Response
### Success:
##### status 200
##### body:

##### Успешных ответов всего 3:

Тревоги нет
```json
{
  "state": "NO_ALARM"
}
```


Тревога есть

```json
{
  "state": "ACTIVE_ALARM"
}
```


Частичная тревога
```json
{
  "state": "PARTIAL_ALARM"
}
```

### Error:

##### status 500

##### body:

```json
{
  "message": "Redirect error"
}
```

Полный список регионов
https://docs.google.com/spreadsheets/d/1XnTOzcPHd1LZUrarR1Fk43FUyl8Ae6a6M7pcwDRjNdA/edit?gid=0#gid=0