### Авторизированные запросы, нуждаются в заголовке Authorization со значением "Bearer " + accessToken
#### Example:
```
Authorization : Bearer d4a76068f5104f26975499d22bcd11cc1665995491673
```
## 1.1. Получить информацию о себе
## GET
#####  /api/v1/profile/userData

### Response
### Success:
##### status 200
##### body:
```json
{
  "email": "user1@example.com",
  "username": "user1",
  "company": "Company A"
}
```
### Error:
##### status 400






## 1.2. Изменить учетные данные

## POST
#####  /api/v1/profile/updateUserCreds
### Request
##### body:
```json
{
  "username": "newUsername",
  "password": "userpassword",
  "newPassword": "newUserpassword",
  "email" : "newmail@examle.com"
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
  "message": "Incorrect credentials",
  "errors": {
    "password": "Password contains prohibited characters"
  }
}
```

## 1.3. Изменить неучетную информацию о пользователе

## POST
#####  /api/v1/profile/updateUserInfo
### Request
##### body:
```json
{
  "company": "newCompany"
}
```

### Response
### Success:
##### status 200


### Error:
##### status 400
##### body:
```json
{
  "code": "BAD_CREDENTIALS",
  "message": "Incorrect credentials",
  "errors": {
    "company": "Company must contain 100 characters maximum"
  }
}
```


## 1.4. Получить список своих штампов

## GET
#####  /api/v1/device/timestamp
### Request
_**parameters**_

```
- int page (defaultValue = "1")
- int size (defaultValue = "50")
- long beginTime (required = false)
- long endTime (required = false)
- string deviceModel (required = false)
- string deviceSerial (required = false)
```
### Response

### Success:
##### status 200
##### body:
```json
{
  "content": [
    {
      "unixtime": 1700004000,
      "deviceModel": "Model A",
      "deviceSerial": "Serial-1-E",
      "firmwareVersion": "v1.4"
    },
    {
      "unixtime": 1700003000,
      "deviceModel": "Model A",
      "deviceSerial": "Serial-1-D",
      "firmwareVersion": "v1.3"
    }
  ],
  "page": 1,
  "size": 50,
  "totalElements": 2,
  "totalPages": 1,
  "last": true
}
```

### Error
##### status 400


## 1.5. Записать штамп

## POST
#####  /api/v1/device/timestamp
### Request
##### body:
```json
{
  "unixtime": 1700004000,
  "deviceModel": "Model A",
  "deviceSerial": "Serial-1-E",
  "firmwareVersion": "v1.4",
  "testResult": {}
}
```

### Response
### Success:
##### status 200

### Error
##### status 400
