# Guide: Docker

## What it is

Docker is used in two ways in this project:

- **In development**, Quarkus Dev Services starts a PostgreSQL container automatically. Nothing to configure.
- **For the full stack** (as in preprod and prod), Docker Compose runs the database and the backend, defined in `docker-compose.yml` at the root of the repository.

| Service | Image / build | Role | Port (host → container) |
|---|---|---|---|
| `db` | `postgres:16` | PostgreSQL database | `5433 → 5432` |
| `backend` | built from `backend/Dockerfile` | Quarkus REST API | `8080 → 8080` |

The frontend is not containerized.

## Prerequisites

- Docker Desktop running, with WSL integration enabled when working under WSL.
- For Docker Compose only: a `.env` file at the root of the repository (see below).

## Development: Quarkus Dev Services

```bash
cd backend
./mvnw quarkus:dev
```

Quarkus starts a PostgreSQL container by itself, then Flyway applies the schema (`db/migration`) and the fictional demo data (`db/demo-data`). No `.env` and no `docker compose` are needed.

| Setting | Value |
|---|---|
| Host | `localhost` |
| Port | `5432` |
| Database | `echange_temps` |
| User / password | `dev` / `dev` |

These values are set in `backend/src/main/resources/application.properties` (`quarkus.datasource.devservices.*`). The port is fixed in dev so that a SQL client (DBeaver, IntelliJ) always finds the database. Tests use a random port, so they can run while `quarkus:dev` is running.

The dev database is a throwaway: it is recreated with fresh demo data each time `quarkus:dev` starts.

## Full stack: Docker Compose

### Configuration

Create your `.env` from the example and fill in the values:

```bash
cp .env.example .env
```

| Variable | Used for |
|---|---|
| `DB_NAME` | Database name |
| `DB_USER` | Database user |
| `DB_PASSWORD` | Database password |
| `FLYWAY_LOCATIONS` | Flyway migration folders built into the backend image (see below) |

`.env` contains secrets and must never be committed. Check it is ignored (the command must print `.env`):

```bash
git check-ignore .env
```

### Demo data: `FLYWAY_LOCATIONS`

| Value | Result |
|---|---|
| `db/migration` (default) | Schema only, empty database |
| `db/migration,db/demo-data` | Schema + fictional demo data |

Never load demo data in an environment with real users.

`FLYWAY_LOCATIONS` is a build argument: after changing it, rebuild the backend image (`--build`).

### Run

```bash
docker compose up -d --build
```

The backend container waits for the database healthcheck before starting.

Do not run it at the same time as `quarkus:dev`: both use port `8080`. To keep only the database, stop the backend container with `docker compose stop backend`.

### Accessing the database

From the terminal:

```bash
docker compose exec db sh -c 'psql -U "$POSTGRES_USER" -d "$POSTGRES_DB"'
```

From a SQL client: host `localhost`, port `5433`, database, user and password from `.env`.

The Compose database uses port `5433` on the host, so it does not conflict with the Dev Services database on `5432`. Inside the Docker network, the backend reaches it at `db:5432`.

### Useful commands

| Command | Effect |
|---|---|
| `docker compose ps` | List running services and their status |
| `docker compose logs -f backend` | Follow backend logs |
| `docker compose stop` | Stop services, keep containers and data |
| `docker compose down` | Remove containers, keep data |
| `docker compose up -d --build backend` | Rebuild the backend image after code changes |
| `docker compose down -v` | Remove containers **and delete all database data** |

### Reset the database from scratch

```bash
docker compose down -v
docker compose up -d --build
```

The volume `postgres_data` is deleted, so the database starts empty. Flyway recreates the schema (and the demo data if enabled) when the backend starts.

### Good to know

- `restart: unless-stopped`: containers restart automatically when Docker Desktop starts, until you stop them.
- Code changes are not visible in the backend container until the image is rebuilt.

## Troubleshooting

| Problem | Fix |
|---|---|
| `Could not find a valid Docker environment` | Docker Desktop is not running, or WSL integration is disabled |
| `Port 5432 is already in use` (dev) | A local PostgreSQL or another container uses it: stop it |
| `Port 8080 is already in use` | Stop the backend container: `docker compose stop backend` |
| `Port 5433 is already in use` | Another service uses it: stop it or change the host port in `docker-compose.yml` |
| `The "DB_NAME" variable is not set` | The `.env` file is missing or incomplete |
| Backend container keeps restarting | Check logs: `docker compose logs backend` |
