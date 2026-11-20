# ---------- Stage 1: build ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -q package -DskipTests

# ---------- Stage 2: run ----------
FROM eclipse-temurin:21-jre-alpine
RUN addgroup -S app && adduser -S app -G app
WORKDIR /app
COPY --from=build --chown=app:app /build/target/quarkus-app/lib/     ./lib/
COPY --from=build --chown=app:app /build/target/quarkus-app/*.jar    ./
COPY --from=build --chown=app:app /build/target/quarkus-app/app/     ./app/
COPY --from=build --chown=app:app /build/target/quarkus-app/quarkus/ ./quarkus/
USER app
EXPOSE 8080
HEALTHCHECK --interval=10s --timeout=3s --start-period=20s --retries=5 \
    CMD wget -qO- http://localhost:8080/q/health/ready || exit 1
ENTRYPOINT ["java", "-jar", "/app/quarkus-run.jar"]
