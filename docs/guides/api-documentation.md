# Guide: API documentation (OpenAPI / Swagger UI)

## What it is

- **OpenAPI**: a standard description of the REST API (routes, parameters, request and response bodies, status codes).
- **Swagger UI**: an interactive web page generated from it, to browse and test the API.

Both are generated automatically from the backend code by the Quarkus `smallrye-openapi` extension.

## How to access it

Start the backend in dev mode:

```bash
cd backend
./mvnw quarkus:dev
```

| What | URL | Available in |
|---|---|---|
| Swagger UI | http://localhost:8080/q/swagger-ui | dev only |
| OpenAPI (YAML) | http://localhost:8080/q/openapi | all environments |
| OpenAPI (JSON) | http://localhost:8080/q/openapi\?format\=json | all environments |

## How to enrich it

Annotate resources with MicroProfile OpenAPI annotations:

- `@Tag` on the resource class: groups routes by feature.
- `@Operation(summary = "...")` on each method: describes the route.
- `@APIResponse(responseCode = "...", description = "...")`: documents possible responses.

General info (title, version, description) is set in `application.properties` with the `quarkus.smallrye-openapi.info-*` properties.
