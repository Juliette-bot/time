# Two environments (preprod and prod) with feature branches

- Status: accepted
- Date: 2026-11

## Context and Problem Statement

How can new work be tested online while always keeping a working version available?

## Considered Options

- A single environment deployed from `main`
- Two environments, `preprod` and `prod`, with one branch per feature

## Decision Outcome

Chosen option: "Two environments", because a working version always stays online in prod while new features are validated in preprod.

- Each feature is developed on its own branch (e.g. `feature/create-listing`).
- Feature branches are merged into `preprod`, the default branch on GitHub.
- Only branches in use are kept, to keep the branch tree clean.
- Both environments use fictional demo data, in French.

### Consequences

- Good: prod stays stable; preprod is a safe place to test.
- Bad: two environments to deploy and maintain on the VPS.
