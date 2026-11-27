# Time

Open-source mutual aid time-exchange platform: people offer and request services, and one hour given equals one hour received.

## Stack

| Part | Technology |
|---|---|
| Backend | Java, Quarkus, Hibernate ORM with Panache, Flyway |
| Database | PostgreSQL |
| Frontend | Angular, Tailwind CSS |
| External API | French government address API |

## Repository structure

```
backend/             Quarkus REST API
frontend/            Angular application
docs/                Documentation (ADRs, guides)
docker-compose.yml   Local services
```

## Getting started

```bash
docker compose up -d db
cd backend
./mvnw quarkus:dev
```

- API: http://localhost:8080
- Swagger UI: http://localhost:8080/q/swagger-ui
- Quarkus Dev UI: http://localhost:8080/q/dev/

## Branches and environments

- `preprod` (default branch): pre-production environment
- `prod`: production environment
- One branch per feature (e.g. `feature/create-listing`), merged into `preprod` through a pull request

## Documentation

All documentation is in [`docs/`](docs/README.md): architecture decisions, guides and API documentation.
