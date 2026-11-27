# Documentation conventions

How documentation is organized in this repository. Follow these rules when adding or changing docs.

## Where things go

| Content | Location | Question it answers |
|---|---|---|
| Project overview, quick start | `README.md` (root) | What is it, how do I start? |
| Backend overview | `backend/README.md` | How do I run and understand the backend? |
| Frontend overview | `frontend/README.md` | How do I run and understand the frontend? |
| Architecture decisions | `docs/adr/` | **Why** is it built this way? |
| Guides (tools, procedures) | `docs/guides/` | **How** do I use or do this? |
| Documentation index | `docs/README.md` | Where is everything? |

READMEs stay short and link to `docs/` for details.

## Language

- All documentation is written in **English** (code, docs, ADRs, commit messages).
- User-facing app content and demo data are in French.

## ADRs

- Format: MADR, copied from `docs/adr/template.md`.
- File name: `NNNN-short-title-in-kebab-case.md`, next free number.
- Sections: Context and Problem Statement, Considered Options, Decision Outcome, Consequences (Good / Bad).
- An accepted ADR is never edited: write a new one that supersedes it.
- Write one when a decision is hard to reverse or affects the whole project.

## Guides

- File: `docs/guides/<topic>.md`, title `# Guide: <Topic>`.
- Usual sections, as needed: What it is, Why, Prerequisites, How to access / run, Useful commands, Good to know, Troubleshooting.
- Prefer tables for commands, URLs, ports and settings.
- Every new guide gets a line in `docs/README.md`.

## Workflow

- One branch per change, from `preprod`: `docs/<topic>`.
- Conventional Commits: `docs: <what changed>`.
- Pull request to `preprod`, then delete the branch.
- When a tool or procedure changes, update its guide in the same pull request.
