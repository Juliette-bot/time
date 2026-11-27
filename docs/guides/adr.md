# Guide: Architecture Decision Records

## What it is

An ADR is a short Markdown file that records one significant technical decision: context, options considered, choice and consequences. ADRs live in `docs/adr/` and follow the MADR format.

## Why

- Keep track of *why* a choice was made, not only *what* was done.
- Help newcomers understand the architecture without asking.
- Avoid reopening decisions that were already made.

## How to read them

```bash
npx log4brains preview
```

Then open the URL shown in the terminal.

## How to write a new one

```bash
npx log4brains adr new
```

Or copy `docs/adr/template.md` to `docs/adr/XXXX-short-title.md` (next number).

## When to write one

When a decision is hard to reverse or affects the whole project: framework, architecture, database, deployment, security, data privacy.

A decision is never edited once accepted: write a new ADR that supersedes it.
