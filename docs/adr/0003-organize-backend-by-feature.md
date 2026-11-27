# Organize backend code by feature

- Status: accepted
- Date: 2026-11

## Context and Problem Statement

How should the Java packages of the backend be structured?

## Considered Options

- Package by layer (`resource/`, `service/`, `repository/`)
- Package by feature (`user/`, `listing/`, `discussion/`, `transaction/`)

## Decision Outcome

Chosen option: "Package by feature", because all the code of a business domain stays together, which makes each feature easier to find, understand and change.

### Consequences

- Good: high cohesion; one feature branch mostly touches one package.
- Good: the structure reflects the business domain.
- Bad: shared code needs a clear home (e.g. a `common/` package).
