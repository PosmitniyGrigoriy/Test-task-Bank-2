## О приложении
1. Клиента можно добавить в систему без JWT. Остальные методы (пополнение баланса, снятие, перевод) работают только с JWT.
2. Планировщик запускается каждую минуту. Повышает на указанный % банковский счет всех клиентов до определенного %.
3. При запуске приложения во всех таблицах появятся тестовые данные. Будет 10 пользователей, 10 банковских счетов, 60 операций (пополнение, списание, перевод), а также заполненная таблица с историей изменений контактных данных клиентов.
4. Для работы приложения нужно 2 базы данных: одна для работы тестов, другая для работы приложения.

## Скриншоты
![swagger-ui](/pic/swagger-ui.png)

![clients](/pic/clients.png)

![bank_accounts](/pic/bank_accounts.png)

![bank_operations](/pic/bank_operations.png)

![contact_data_history](/pic/contact_data_history.png)

## Подготовка к запуску приложения
Добавляем пользователя в БД
```
CREATE ROLE "eYRtj6P2pTbVRjRu8VdZ" WITH LOGIN PASSWORD '7EP3FuPq8FVcdbY1wMb';
```

Выдаем нашему пользователю права супер пользователя
```
ALTER ROLE "eYRtj6P2pTbVRjRu8VdZ" WITH SUPERUSER;
```

Создаем БД
```
CREATE DATABASE bank_db;
CREATE DATABASE bank_test_db;
```

Создаем схему в первой и второй БД
```
CREATE SCHEMA public;
```

## Ссылка для Swagger v3
```
http://localhost:8080/swagger-ui/index.html
```

## Важно:
1. В таблицах (BANK_ACCOUNTS и BANKING_OPERATIONS) есть поля, где пишется сумма. Там указано в копейках.
2. БД есть пользователь с id = 1000. Миронов Михаил Петрович. С балансом 14300000 копеек. Можно зайти под ним, введя JWT в Swagger, и опробовать API-методы. Токен создан 26 мая 2024. Действителен 365 дней.
```
eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJwZ2EucHJvZmlsZUBnbWFpbC5jb20iLCJhdWQiOiJiYW5rLnJ1IiwiY2kiOiLQnNC40YDQvtC90L7QsiDQnNC40YXQsNC40Lsg0J_QtdGC0YDQvtCy0LjRhzoxOTkxLTA5LTEwOm1pcm9ub3YtbXAxOTkxIiwic2NvcGUiOiJST0xFX0NMSUVOVCIsImlzcyI6IkdyaWdvcml5IFBvc21pdG5peSIsImV4cCI6MTc0ODI1OTEwMCwiaWF0IjoxNzE2NzIzMTAwfQ.W98n_tqqNLx5lhlU22QTICLHilCHIwTn3-LegO5ZDXjRNPIZY5f0yoexXjZ_QikR_Qtbeyx_4qbyJMzgbINKIq_b4Cln1GhfV6fDBJUTMEUYOVSfZXSyurAEtgUkkwbanajXpWRdNYoFgSXkt4n4ebK5yVn_uugH35SNlU7uMtYBb57OqZosbgWph9OpJL0MzZdvdGWUt2Gm-KX_4ngPR53lu-LcjaxAUBH5vhuwts_edFvfroRoPyh_1QP4-A0hXjwYULDkvMHf9RoGCqsoU1A1zyijbqLKEJQ6w6nCrgS6HvQsb2UfYoHrLvO1ZiedsAEnnza5lPDpEOvgA7gBrA
```

## Пример заполненного JSON для добавления клиента (метод POST /api/clients/add)
```
{
  "lastName": "Антонов",
  "firstName": "Алексей",
  "patronymic": "Олегович",
  "birthDate": "1990-10-17",
  "phoneNumbers": [
    "+7-924-333-22-44"
  ],
  "emails": [
    "aao@mail.ru"
  ],
  "login": "mylogin",
  "password": "password",
  "bankAccountRequestDto": {
    "amount": 50000
  },
  "jwtRequestDto": {
    "timeValue": 7,
    "timeUnit": "DAYS"
  }
}
```
При добавлении клиента нужно указать длительность выдачи токена.

## Приложение разработано на Java 17

## Зависимости
| Название                                   | Версия  |
|--------------------------------------------|---------|
| spring-boot-starter-parent                 | 3.0.2   |
| spring-boot-starter-data-jpa               | 3.0.2   |
| spring-boot-starter-web                    | 3.0.2   |
| spring-boot-starter-oauth2-resource-server | 3.0.2   |
| spring-boot-configuration-processor        | 3.0.2   |
| spring-boot-starter-validation             | 3.0.2   |
| spring-boot-starter-test                   | 3.0.2   |
| junit                                      | 4.13.2  |
| lombok                                     | 1.18.32 |
| springdoc-openapi-starter-webmvc-ui        | 2.5.0   |
| postgresql                                 | 42.5.1  |
| mapstruct                                  | 1.5.5   |
| flyway-core                                | 9.5.1   |

## Тесты написаны на JUnit

## Команда для запуска приложения без тестов
```
mvn spring-boot:run
```

## Команда для запуска приложения с тестами
```
mvn clean install spring-boot:run
```

## Контакты
| Имя      | Номер телефона   | Телеграм           | Электронная почта     |
|----------|------------------|--------------------|-----------------------|
| Григорий | 8-924-116-18-34  | posmitniy_grigoriy | pga.profile@gmail.com |