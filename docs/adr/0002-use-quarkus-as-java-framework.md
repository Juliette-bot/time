# Use Quarkus as the Java backend framework

- Status: accepted
- Date: 2026-11

## Context and Problem Statement

Once Java is chosen (see [ADR-0001](0001-switch-backend-from-python-to-java.md)), which framework should build the REST API? The app will run on a small VPS.

## Considered Options

- Spring Boot
- Quarkus

## Decision Outcome

Chosen option: "Quarkus", because it starts fast and uses little memory, which suits a modest VPS, and its dev mode (live reload, Dev Services) speeds up development.

### Consequences

- Good: low resource usage in preprod and prod.
- Good: built-in extensions (Hibernate ORM with Panache, Flyway, SmallRye OpenAPI).
- Bad: smaller community and fewer tutorials than Spring Boot.
