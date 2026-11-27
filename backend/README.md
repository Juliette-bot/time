# Time — Backend

Quarkus REST API of the Time platform. See the [main README](../README.md) for the project overview.

## Architecture

Code is organized by feature (`user`, `listing`, `discussion`, `transaction`), each with Resource / Service / Repository layers and separate request/response DTOs. See [ADR-0003](../docs/adr/0003-organize-backend-by-feature.md) and [ADR-0004](../docs/adr/0004-layered-feature-with-panache-repository.md).

## Run in dev mode

```bash
./mvnw quarkus:dev
```

| What | URL |
|---|---|
| API | http://localhost:8080 |
| Swagger UI | http://localhost:8080/q/swagger-ui |
| Dev UI | http://localhost:8080/q/dev/ |
| Health check | http://localhost:8080/q/health |

## Database migrations

Schema is managed by Flyway: migration scripts are in `src/main/resources/db/migration/`.

## Package

```bash
./mvnw package
java -jar target/quarkus-app/quarkus-run.jar
```

## Extensions

Hibernate ORM with Panache, REST Jackson, Hibernate Validator, Flyway, PostgreSQL JDBC, SmallRye Health, SmallRye OpenAPI.

More: [API documentation guide](../docs/guides/api-documentation.md).
