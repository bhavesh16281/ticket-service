# Ticket Service

A RESTful backend service for managing customer support tickets, built with **Spring Boot**, **Spring Data JPA** and **MySQL**. It lets you create, retrieve, update the status of, and delete support tickets, with request validation and a consistent JSON error format.

The `Ticket` entity also contains placeholder fields for **AI-derived metadata** (`priority`, `sentiment`, `intent`, `suggestedTeam`). They are stored and returned by the API but are not populated yet, so they can be filled in by a future AI/ML integration.

---

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [API Reference](#api-reference)
- [Data Model](#data-model)
- [Validation Rules](#validation-rules)
- [Error Handling](#error-handling)
- [Monitoring (Actuator)](#monitoring-actuator)
- [Running Tests](#running-tests)
- [Troubleshooting](#troubleshooting)
- [Roadmap](#roadmap)

---

## Features

- Create support tickets (new tickets always start as `OPEN`)
- Fetch a single ticket by ID
- List all tickets with pagination and sorting
- Update a ticket's status (`OPEN`, `IN_PROGRESS`, `RESOLVED`, `CLOSED`)
- Delete a ticket
- Bean Validation on incoming requests
- Centralised exception handling that returns a consistent JSON error body
- Automatic `createdAt` / `updatedAt` timestamps
- Spring Boot Actuator endpoints for health and metrics
- Schema auto-generation via Hibernate (`ddl-auto: update`)

## Tech Stack

| Area | Technology |
|------|------------|
| Language | Java 21 |
| Framework | Spring Boot 4.1.1 |
| Web | Spring Web MVC (REST controllers) |
| Persistence | Spring Data JPA / Hibernate |
| Database | MySQL (runtime driver `mysql-connector-j`); H2 is also on the classpath |
| Validation | Jakarta Bean Validation (`spring-boot-starter-validation`) |
| Monitoring | Spring Boot Actuator |
| Boilerplate reduction | Lombok |
| Build tool | Maven (Maven Wrapper included) |
| Testing | Spring Boot Test starters (JUnit) |

## Project Structure

```
ticket-service/
├── pom.xml
├── mvnw / mvnw.cmd                  # Maven wrapper scripts
└── src/
    ├── main/
    │   ├── java/com/bhavesh16281/ticket_service/
    │   │   ├── TicketServiceApplication.java   # Application entry point
    │   │   ├── controller/
    │   │   │   └── TicketController.java       # REST endpoints
    │   │   ├── service/
    │   │   │   └── TicketService.java          # Business logic / transactions
    │   │   ├── repository/
    │   │   │   └── TicketRepository.java       # Spring Data JPA repository
    │   │   ├── entity/
    │   │   │   ├── Ticket.java                 # JPA entity (table: tickets)
    │   │   │   └── TicketStatus.java           # Status enum
    │   │   ├── dto/
    │   │   │   ├── CreateTicketRequest.java    # Create payload (validated)
    │   │   │   ├── UpdateTicketStatusRequest.java
    │   │   │   └── TicketResponse.java         # API response model
    │   │   └── exception/
    │   │       ├── GlobalExceptionHandler.java # @RestControllerAdvice
    │   │       ├── ApiError.java               # Error response body
    │   │       └── ResourceNotFoundException.java
    │   └── resources/
    │       └── application.yaml                # App, DB, JPA and Actuator config
    └── test/java/.../TicketServiceApplicationTests.java
```

The code follows a classic layered design: **Controller → Service → Repository → Database**. DTOs (Java records) keep the API contract separate from the JPA entity.

## Prerequisites

- **JDK 21** (or newer)
- **MySQL 8+** running locally (or reachable from your machine)
- **Git**
- Maven is optional because the included wrapper (`mvnw` / `mvnw.cmd`) downloads it automatically
- A REST client such as Postman or curl (optional)

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/bhavesh16281/ticket-service.git
cd ticket-service
```

### 2. Create the MySQL database

```sql
CREATE DATABASE ticketdb;
```

The `tickets` table is created automatically on first startup (`ddl-auto: update`).

### 3. Configure database credentials

The defaults in [`src/main/resources/application.yaml`](src/main/resources/application.yaml) are:

| Setting | Default |
|---------|---------|
| URL | `jdbc:mysql://localhost:3306/ticketdb` |
| Username | `root` |
| Password | `root` |

Update these to match your MySQL setup. See [Configuration](#configuration) for how to override them without editing the file.

### 4. Run the application

**Windows (PowerShell / CMD):**

```powershell
.\mvnw.cmd spring-boot:run
```

**macOS / Linux:**

```bash
./mvnw spring-boot:run
```

The service starts on **http://localhost:8080**.

### 5. Build a runnable JAR (optional)

```bash
./mvnw clean package
java -jar target/ticket-service-0.0.1-SNAPSHOT.jar
```

## Configuration

All settings live in [`application.yaml`](src/main/resources/application.yaml). Any property can be overridden with environment variables, which is the recommended approach for credentials:

```bash
# Linux / macOS
export SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/ticketdb
export SPRING_DATASOURCE_USERNAME=myuser
export SPRING_DATASOURCE_PASSWORD=mypassword
```

```powershell
# Windows PowerShell
$env:SPRING_DATASOURCE_URL="jdbc:mysql://localhost:3306/ticketdb"
$env:SPRING_DATASOURCE_USERNAME="myuser"
$env:SPRING_DATASOURCE_PASSWORD="mypassword"
```

| Property | Purpose |
|----------|---------|
| `server.port` | HTTP port (default `8080`) |
| `spring.datasource.*` | MySQL connection details |
| `spring.jpa.hibernate.ddl-auto` | `update` auto-creates/updates tables (good for development) |
| `spring.jpa.show-sql` | Logs generated SQL |
| `management.endpoints.web.exposure.include` | Actuator endpoints exposed (`*` = all) |

> **Security note:** The committed `root/root` credentials and the fully exposed Actuator endpoints are intended for local development only. Use environment variables or a secrets manager, a dedicated DB user, `ddl-auto: validate` (with a migration tool), and restricted Actuator exposure in any shared or production environment.

## API Reference

Base URL: `http://localhost:8080/api/v1/tickets`

| Method | Endpoint | Description | Success response |
|--------|----------|-------------|------------------|
| `POST` | `/create` | Create a new ticket | `200 OK` + ticket |
| `GET` | `/getbyid/{id}` | Get a ticket by ID | `200 OK` + ticket |
| `GET` | `/getAll` | List tickets (paginated) | `200 OK` + page of tickets |
| `PATCH` | `/{id}/update` | Update a ticket's status | `200 OK` + ticket |
| `DELETE` | `/deleteById/{id}` | Delete a ticket | `204 No Content` |

### Create a ticket

`POST /api/v1/tickets/create`

```bash
curl -X POST http://localhost:8080/api/v1/tickets/create \
  -H "Content-Type: application/json" \
  -d '{
        "subject": "Cannot log in",
        "description": "I get an error every time I try to sign in.",
        "requesterEmail": "user@example.com"
      }'
```

Response:

```json
{
  "id": 1,
  "subject": "Cannot log in",
  "description": "I get an error every time I try to sign in.",
  "requesterEmail": "user@example.com",
  "status": "OPEN",
  "priority": null,
  "sentiment": null,
  "intent": null,
  "suggestedTeam": null,
  "createdAt": "2026-10-11T06:19:58.481Z",
  "updatedAt": "2026-10-11T06:19:58.481Z"
}
```

### Get a ticket by ID

```bash
curl http://localhost:8080/api/v1/tickets/getbyid/1
```

### List tickets (pagination & sorting)

The `getAll` endpoint accepts standard Spring `Pageable` query parameters:

```bash
curl "http://localhost:8080/api/v1/tickets/getAll?page=0&size=10&sort=createdAt,desc"
```

### Update a ticket's status

Valid values: `OPEN`, `IN_PROGRESS`, `RESOLVED`, `CLOSED`.

```bash
curl -X PATCH http://localhost:8080/api/v1/tickets/1/update \
  -H "Content-Type: application/json" \
  -d '{ "status": "IN_PROGRESS" }'
```

### Delete a ticket

```bash
curl -X DELETE http://localhost:8080/api/v1/tickets/deleteById/1
```

## Data Model

Table: `tickets`

| Column | Type | Notes |
|--------|------|-------|
| `id` | `BIGINT` | Primary key, auto-increment |
| `subject` | `VARCHAR(255)` | Required |
| `description` | `VARCHAR(5000)` | Required |
| `requester_email` | `VARCHAR(255)` | Required |
| `status` | `VARCHAR(50)` | Enum: `OPEN`, `IN_PROGRESS`, `RESOLVED`, `CLOSED` (default `OPEN`) |
| `priority` | `VARCHAR(50)` | AI-derived, currently unset |
| `sentiment` | `VARCHAR(50)` | AI-derived, currently unset |
| `intent` | `VARCHAR(50)` | AI-derived, currently unset |
| `suggested_team` | `VARCHAR(50)` | AI-derived, currently unset |
| `created_at` | `TIMESTAMP` | Set automatically on insert |
| `updated_at` | `TIMESTAMP` | Updated automatically on every update |

## Validation Rules

`POST /create`:

| Field | Rules |
|-------|-------|
| `subject` | Not blank, 5–200 characters |
| `description` | Not blank, 10–4000 characters |
| `requesterEmail` | Not blank, valid email format |

`PATCH /{id}/update`:

| Field | Rules |
|-------|-------|
| `status` | Required, must be a valid `TicketStatus` value |

## Error Handling

Errors raised through the `GlobalExceptionHandler` are returned in this format:

```json
{
  "timestamp": "2026-10-11T06:25:00.123Z",
  "status": 404,
  "message": "Ticket not found with id: 1",
  "path": "/api/v1/tickets/getbyid/1"
}
```

| Scenario | HTTP status |
|----------|-------------|
| Ticket ID does not exist (`ResourceNotFoundException`) | `404 Not Found` |
| Request body fails validation (`MethodArgumentNotValidException`) | `400 Bad Request` |

Other errors (for example `405 Method Not Allowed` or malformed JSON) are not yet handled by `GlobalExceptionHandler`, so Spring Boot's default error body is returned instead, which looks like `{ "timestamp", "status", "error", "path" }`.

## Monitoring (Actuator)

Actuator endpoints are exposed at `/actuator`, for example:

- Health: http://localhost:8080/actuator/health
- Metrics: http://localhost:8080/actuator/metrics
- Info: http://localhost:8080/actuator/info

## Running Tests

```bash
./mvnw test        # macOS / Linux
.\mvnw.cmd test    # Windows
```

> The default context-load test needs a reachable MySQL database with the configured credentials, because the datasource points to MySQL.

## Troubleshooting

| Problem | Likely cause / fix |
|---------|--------------------|
| `Communications link failure` / app fails to start | MySQL is not running or the URL, port, or credentials are wrong |
| `Unknown database 'ticketdb'` | Create the database: `CREATE DATABASE ticketdb;` |
| `405 Method Not Allowed` | Wrong HTTP method; for example `/create` requires `POST` |
| `400 Bad Request` on create | Request body fails validation; check the field rules above |
| `404 Not Found` on get/update/delete | No ticket with that ID exists |
| Port 8080 already in use | Change `server.port` in `application.yaml` |

## Roadmap

- Populate AI-derived fields (`priority`, `sentiment`, `intent`, `suggestedTeam`) from an AI/ML service
- Add handlers for more exceptions (405, malformed JSON, type mismatch, generic 500)
- Add unit and integration tests for the service and controller layers
- Switch to database migrations (Flyway/Liquibase) and externalised configuration for production
- Add authentication/authorisation
