# Guide: Docker

## What it is

Docker Compose runs the project services in containers, defined in `docker-compose.yml` at the root of the repository.

| Service | Image / build | Role | Port (host → container) |
|---|---|---|---|
| `db` | `postgres:16` | PostgreSQL database | `5433 → 5432` |
| `backend` | built from `backend/Dockerfile` | Quarkus REST API | `8080 → 8080` |

The frontend is not containerized.

## Prerequisites

- Docker Desktop, with WSL integration enabled when working under WSL.
- A `.env` file at the root of the repository (see below).

## Configuration

Create your `.env` from the example and fill in the values:

```bash
cp .env.example .env
```

| Variable | Used for |
|---|---|
| `DB_NAME` | Database name |
| `DB_USER` | Database user |
| `DB_PASSWORD` | Database password |
| `FLYWAY_LOCATIONS` | Flyway migration folders included in the backend build (default: `db/migration`) |

`.env` contains secrets and must never be committed. Check it is ignored:

```bash
git check-ignore .env
```

## Two ways to run the project

### Development (recommended): database in Docker, backend in dev mode

```bash
docker compose up -d db
cd backend
./mvnw quarkus:dev
```

The backend runs with live reload. It connects to the database on `localhost:5433`.

### Full stack: everything in Docker

```bash
docker compose up -d --build
```

Use this to check the app as it will run in preprod or prod. The backend container waits for the database healthcheck before starting.

Do not run both at the same time: the backend container and `quarkus:dev` both use port `8080`.

## Accessing the database

From the terminal:

```bash
docker compose exec db sh -c 'psql -U "$POSTGRES_USER" -d "$POSTGRES_DB"'
```

From IntelliJ (Database tool window) or any SQL client:

- Host: `localhost`
- Port: `5433`
- Database, user, password: values from `.env`

Port `5433` is used on the host to avoid conflicts with a local PostgreSQL on `5432`. Inside the Docker network, the backend reaches the database at `db:5432`.

## Useful commands

| Command | Effect |
|---|---|
| `docker compose ps` | List running services and their status |
| `docker compose logs -f backend` | Follow backend logs |
| `docker compose stop` | Stop services, keep containers and data |
| `docker compose down` | Remove containers, keep data |
| `docker compose up -d --build backend` | Rebuild the backend image after code changes |
| `docker compose down -v` | Remove containers **and delete all database data** |

## Reset the database from scratch

```bash
docker compose down -v
docker compose up -d db
```

The volume `postgres_data` is deleted, so the database starts empty. Flyway recreates the schema and the demo data at the next backend start.

## Good to know

- `restart: unless-stopped`: containers restart automatically when Docker Desktop starts, until you stop them.
- `FLYWAY_LOCATIONS` is a build argument: changing it requires rebuilding the backend image (`--build`).
- Code changes are not visible in the backend container until the image is rebuilt.

## Troubleshooting

| Problem | Fix |
|---|---|
| `Port 8080 is already in use` | Stop the backend container: `docker compose stop backend` |
| `Port 5433 is already in use` | Another service uses it: stop it or change the host port in `docker-compose.yml` |
| `The "DB_NAME" variable is not set` | The `.env` file is missing or incomplete |
| Backend container keeps restarting | Check logs: `docker compose logs backend` |
EOFcat > docs/guides/docker.md << 'EOF'
# Guide: Docker

## What it is

Docker Compose runs the project services in containers, defined in `docker-compose.yml` at the root of the repository.

| Service | Image / build | Role | Port (host → container) |
|---|---|---|---|
| `db` | `postgres:16` | PostgreSQL database | `5433 → 5432` |
| `backend` | built from `backend/Dockerfile` | Quarkus REST API | `8080 → 8080` |

The frontend is not containerized.

## Prerequisites

- Docker Desktop, with WSL integration enabled when working under WSL.
- A `.env` file at the root of the repository (see below).

## Configuration

Create your `.env` from the example and fill in the values:

```bash
cp .env.example .env
```

| Variable | Used for |
|---|---|
| `DB_NAME` | Database name |
| `DB_USER` | Database user |
| `DB_PASSWORD` | Database password |
| `FLYWAY_LOCATIONS` | Flyway migration folders included in the backend build (default: `db/migration`) |

`.env` contains secrets and must never be committed. Check it is ignored:

```bash
git check-ignore .env
```

## Two ways to run the project

### Development (recommended): database in Docker, backend in dev mode

```bash
docker compose up -d db
cd backend
./mvnw quarkus:dev
```

The backend runs with live reload. It connects to the database on `localhost:5433`.

### Full stack: everything in Docker

```bash
docker compose up -d --build
```

Use this to check the app as it will run in preprod or prod. The backend container waits for the database healthcheck before starting.

Do not run both at the same time: the backend container and `quarkus:dev` both use port `8080`.

## Accessing the database

From the terminal:

```bash
docker compose exec db sh -c 'psql -U "$POSTGRES_USER" -d "$POSTGRES_DB"'
```

From IntelliJ (Database tool window) or any SQL client:

- Host: `localhost`
- Port: `5433`
- Database, user, password: values from `.env`

Port `5433` is used on the host to avoid conflicts with a local PostgreSQL on `5432`. Inside the Docker network, the backend reaches the database at `db:5432`.

## Useful commands

| Command | Effect |
|---|---|
| `docker compose ps` | List running services and their status |
| `docker compose logs -f backend` | Follow backend logs |
| `docker compose stop` | Stop services, keep containers and data |
| `docker compose down` | Remove containers, keep data |
| `docker compose up -d --build backend` | Rebuild the backend image after code changes |
| `docker compose down -v` | Remove containers **and delete all database data** |

## Reset the database from scratch

```bash
docker compose down -v
docker compose up -d db
```

The volume `postgres_data` is deleted, so the database starts empty. Flyway recreates the schema and the demo data at the next backend start.

## Good to know

- `restart: unless-stopped`: containers restart automatically when Docker Desktop starts, until you stop them.
- `FLYWAY_LOCATIONS` is a build argument: changing it requires rebuilding the backend image (`--build`).
- Code changes are not visible in the backend container until the image is rebuilt.

## Troubleshooting

| Problem | Fix |
|---|---|
| `Port 8080 is already in use` | Stop the backend container: `docker compose stop backend` |
| `Port 5433 is already in use` | Another service uses it: stop it or change the host port in `docker-compose.yml` |
| `The "DB_NAME" variable is not set` | The `.env` file is missing or incomplete |
| Backend container keeps restarting | Check logs: `docker compose logs backend` |
