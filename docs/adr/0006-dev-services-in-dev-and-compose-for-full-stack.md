# Quarkus Dev Services in development, Docker Compose for the full stack

- Status: accepted
- Date: 2026-11

## Context and Problem Statement

The backend needs a PostgreSQL database both in development and when running the full stack as in preprod and prod. How should the database be provided in each case, without the two setups getting in each other's way?

## Considered Options

- Docker Compose for everything, including development
- Quarkus Dev Services in development, Docker Compose for the full stack

## Decision Outcome

Chosen option: "Quarkus Dev Services in development, Docker Compose for the full stack", because development needs zero setup and a fresh database, while the full stack must run as close as possible to preprod and prod.

- **Development**: `quarkus:dev` starts a throwaway PostgreSQL container automatically, with schema and fictional demo data applied by Flyway. No `.env` needed.
- **Full stack**: `docker compose` runs the database and the backend image built from `backend/Dockerfile`, configured through `.env`. Demo data is included only if `FLYWAY_LOCATIONS` asks for it.
- **Separate ports**: the Dev Services database uses `5432` (fixed, so SQL clients always find it) and the Compose database uses `5433` on the host, so both can run at the same time without conflict. Tests use a random port, so they can run while `quarkus:dev` is running.

### Consequences

- Good: a new contributor only needs Docker running and `./mvnw quarkus:dev`.
- Good: the dev database is always clean and reproducible.
- Good: the full stack is tested with the same image as preprod and prod.
- Bad: two ways to get a database, to document and understand (see [Docker guide](../guides/docker.md)).
- Bad: the backend container and `quarkus:dev` both use port `8080` and cannot run at the same time.
