# Switch backend from Python/Django to Java

- Status: accepted
- Date: 2026-11

## Context and Problem Statement

The backend was started with Python/Django. Which language should the backend use for the rest of the project, until the RNCP defense (June 2027) and beyond?

## Considered Options

- Keep Python/Django
- Switch to Java

## Decision Outcome

Chosen option: "Switch to Java", because it is the language used daily in my work-study position, which lets skills transfer both ways, and it is widely used in the job market for backend roles.

### Consequences

- Good: one main language across work and school project, faster progress.
- Good: strong typing and mature tooling (IntelliJ, Maven/Gradle).
- Bad: the existing Django code is dropped; the database is recreated from scratch with Flyway.
