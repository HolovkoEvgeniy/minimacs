## 1.1. Получить состояние базы данных

## GET
#####  /actuator/health
### Request

### Success:
##### status 200
##### body:
```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "PostgreSQL",
        "validationQuery": "SELECT 1",
        "result": 1
      }
    },
    "diskSpace": {
      "status": "UP",
      "details": {
        "total": 999395905536,
        "free": 265876013056,
        "threshold": 10485760,
        "path": "C:\\LumiRing_projects\\Device_update_manager\\.",
        "exists": true
      }
    },
    "ping": {
      "status": "UP"
    }
  }
}
```



### Error:
##### status 503
##### body:
```json
{
  "status": "DOWN",
  "components": {
    "db": {
      "status": "DOWN",
      "details": {
        "error": "org.springframework.jdbc.CannotGetJdbcConnectionException: Failed to obtain JDBC Connection"
      }
    },
    "diskSpace": {
      "status": "UP",
      "details": {
        "total": 999395905536,
        "free": 265862098944,
        "threshold": 10485760,
        "path": "C:\\LumiRing_projects\\Device_update_manager\\.",
        "exists": true
      }
    },
    "ping": {
      "status": "UP"
    }
  }
}
```