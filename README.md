# Блог - Backend

Бэкенд приложения-блога, реализованный на Java 21 с использованием Spring Framework 6.2 (без Spring Boot) и работающий в сервлет-контейнере Tomcat.
В качестве базы данных используется PostgreSQL 18. 
Бэкенд покрыт тестами (Unit и интеграционными) с использованием JUnit 5, TestContext Framework.

## Стек технологий

- **Фреймворк**: Spring MVC 6.2.14
- **База данных**: PostgreSQL 18 (продакшн), H2 2.3.232 (тестирование)
- **Java**: 21
- **Система сборки**: Gradle 9.2.1 (Kotlin DSL)
- **Тестирование**: JUnit 5, Spring Test, MockMvc
- **JSON**: Jackson с JavaTimeModule
- **Сервер**: Apache Tomcat (развертывание через WAR)

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
Требуется **JDK 21** и **Gradle 9.2** (или другая совместимая версии).

```bash
./gradlew clean build
```

### 3. Запуск тестов

```bash
./gradlew test
```
Все тесты используют встроенную базу данных H2 и не требуют запущенного PostgreSQL.

**Результаты тестов**: 29 пройденных тестов (100% успешных)

### 4. Развертывание в Tomcat

Приложение собирается как WAR-файл для развертывания в Tomcat:

```bash
./gradlew war
```
WAR-файл будет создан в `build/libs/blog-backend.war`.

Скопируйте WAR-файл из папки `build/libs`, переименуйте его в `ROOT.war` и переместите в папку `webapps` внутри корневой папки сервера Tomcat.

При настройках по умолчанию Tomcat в `conf/server.xml` для localhost произойдет автоматическая распаковка и загрузка ROOT.war,
и REST-ендпоинты станут доступны по "localhost:8080/":
```xml
<Host name="localhost"  appBase="webapps"
   unpackWARs="true" autoDeploy="true">
    ...
</Host>
```

При запуске автоматически запустится скрипт `src/main/resources/schema.sql` по созданию необходимых для работы объектов в БД, если объекты еще не были созданы.

## Структура проекта

```
my-blog-back-app/
├── README.md
├── build.gradle.kts
└── src
    ├── main
    │   ├── java
    │   │   └── ru
    │   │       └── practicum
    │   │           └── kobozevva
    │   │               └── blog
    │   │                   ├── configuration
    │   │                   │   ├── DataSourceConfiguration.java
    │   │                   │   ├── MultipartConfiguration.java
    │   │                   │   ├── RestConfiguration.java
    │   │                   │   └── WebConfiguration.java
    │   │                   ├── controller
    │   │                   │   ├── CommentController.java
    │   │                   │   ├── PostController.java
    │   │                   │   └── advice
    │   │                   │       └── GlobalExceptionHandler.java
    │   │                   ├── dto
    │   │                   │   ├── error
    │   │                   │   │   └── ErrorResponseDto.java
    │   │                   │   ├── request
    │   │                   │   │   ├── CommentCreateRequestDto.java
    │   │                   │   │   ├── CommentUpdateRequestDto.java
    │   │                   │   │   ├── PostCreateRequestDto.java
    │   │                   │   │   ├── PostListItemDto.java
    │   │                   │   │   └── PostUpdateRequestDto.java
    │   │                   │   └── response
    │   │                   │       ├── CommentResponseDto.java
    │   │                   │       ├── LikesCountResponseDto.java
    │   │                   │       ├── PostListResponseDto.java
    │   │                   │       └── PostResponseDto.java
    │   │                   ├── mapper
    │   │                   │   ├── CommentMapper.java
    │   │                   │   └── PostMapper.java
    │   │                   ├── model
    │   │                   │   ├── Comment.java
    │   │                   │   ├── Like.java
    │   │                   │   ├── Post.java
    │   │                   │   ├── PostImage.java
    │   │                   │   └── Tag.java
    │   │                   ├── repository
    │   │                   │   ├── CommentRepository.java
    │   │                   │   ├── PostImageRepository.java
    │   │                   │   ├── PostRepository.java
    │   │                   │   ├── TagRepository.java
    │   │                   │   ├── extractor
    │   │                   │   │   └── PostWithTagsExtractor.java
    │   │                   │   ├── jdbc
    │   │                   │   │   ├── JdbcCommentRepository.java
    │   │                   │   │   ├── JdbcPostImageRepository.java
    │   │                   │   │   ├── JdbcPostRepository.java
    │   │                   │   │   └── JdbcTagRepository.java
    │   │                   │   └── rowmapper
    │   │                   │       ├── CommentRowMapper.java
    │   │                   │       ├── PostImageRowMapper.java
    │   │                   │       ├── PostRowMapper.java
    │   │                   │       └── TagRowMapper.java
    │   │                   └── service
    │   │                       ├── CommentService.java
    │   │                       ├── PostImageService.java
    │   │                       ├── PostService.java
    │   │                       ├── TagService.java
    │   │                       └── impl
    │   │                           ├── CommentServiceImpl.java
    │   │                           ├── PostImageServiceImpl.java
    │   │                           ├── PostServiceImpl.java
    │   │                           └── TagServiceImpl.java
    │   ├── resources
    │   │   ├── application.properties
    │   │   ├── logback.xml
    │   │   └── schema.sql
    │   └── webapp
    │       └── WEB-INF
    │           └── web.xml
    └── test
        ├── java
        │   └── ru
        │       └── practicum
        │           └── kobozevva
        │               └── blog
        │                   ├── configuration
        │                   │   ├── TestDataSourceConfiguration.java
        │                   │   └── TestWebApplicationConfiguration.java
        │                   ├── controller
        │                   │   ├── CommentControllerIntegrationTest.java
        │                   │   └── PostControllerIntegrationTest.java
        │                   └── service
        │                       └── impl
        │                           ├── CommentServiceImplTest.java
        │                           └── PostServiceImplTest.java
        └── resources
            ├── application-test.properties
            ├── logback-test.xml
            └── schema-test.sql
```