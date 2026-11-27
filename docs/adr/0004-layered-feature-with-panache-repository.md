# Resource / Service / Repository layers with Panache repository and DTOs

- Status: accepted
- Date: 2026-11

## Context and Problem Statement

Inside each feature (see [ADR-0003](0003-organize-backend-by-feature.md)), how should responsibilities be split, and which Panache pattern should handle persistence?

## Considered Options

- Panache active record (entities extend `PanacheEntity`)
- Panache repository (`PanacheRepository<T>`) with Resource / Service / Repository layers

## Decision Outcome

Chosen option: "Panache repository with layers", because it keeps entities as plain data, separates HTTP, business logic and persistence, and makes services easy to unit-test by mocking repositories.

Each feature also has separate request and response DTOs, so entities are never exposed directly by the API.

### Consequences

- Good: clear separation of concerns and better testability.
- Good: the API contract is decoupled from the database schema.
- Bad: more classes and mapping code than active record.
