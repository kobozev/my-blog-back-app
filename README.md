# Блог - Backend

Бэкенд приложения-блога, реализованный на Java 21 с использованием Spring Boot, Spring Data JDBC и БД PostgreSQL. 

## Стек технологий

- **Фреймворк**: Spring Boot 3.5.10
- **База данных**: PostgreSQL 18 (продакшн), H2 2.3.232 (тестирование)
- **Java**: 21
- **Система сборки**: Maven
- **Тестирование**: JUnit 5, Spring Boot Test, MockMvc

## Сборка и запуск приложения

### 1. Конфигурация приложения

Настройки приложения находятся в файле `src/main/resources/application.properties`, который переопределяет параметры через переменные окружения:

| Переменная окружения | Описание                                |
| --- |-----------------------------------------|
| `spring.datasource.url` | JDBC‑строка подключения к PostgreSQL    |
| `spring.datasource.driverClassName` | Имя класса драйвера JDBC для PostgreSQL |
| `spring.datasource.username` | Пользователь базы данных                |
| `spring.datasource.password` | Пароль пользователя базы данных         |

Проект использует **PostgreSQL** как основную базу данных для продакшн:
`spring.datasource.driverClassName=org.postgresql.Driver`

Настройка по умолчанию подразумевает наличие запущенного PostgreSQL на порту 5432 и созданной БД blog_db с необходимой для работы учетной записью для работы бэкенда с полномочиями на создание объектов в данной БД:  
`spring.datasource.url=jdbc:postgresql://localhost:5432/blog_db`

Инициализация схемы БД (создание таблиц, индексов и т.п.) выполнится автоматически при первом запуске приложения.

### 2. Сборка приложения
Проект можно собрать с помощью Maven:

```bash
./mvnw clean package
```

Итоговый артефакт (executable fat jar) будет в папке "target/". 
Имя артефакта `my-blog-back-app-1.0.0.jar`.

### 3. Запуск приложения
Для запуска приложения достаточно запустить main метод в SpringBootBlogApplication классе или выполнить команду

```bash
java -jar target/my-blog-back-app-1.0.0.jar
```

После запуска API будет доступно по адресу:
```
http://localhost:8080/api/
```