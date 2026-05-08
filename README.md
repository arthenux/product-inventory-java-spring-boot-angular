# Product Inventory

A full-stack product inventory application built with Java, Spring Boot, Angular, MySQL, and JWT authentication.

The project currently includes a secured Spring Boot backend with database-backed users, JWT login, seeded product data, protected Product CRUD endpoints, and an Angular authentication shell with a login flow.

## Tech Stack

| Area | Technology |
| --- | --- |
| Backend | Java 25, Spring Boot 4.0.6 |
| API | Spring Web MVC, Jakarta Validation |
| Security | Spring Security, JWT, BCrypt password hashing |
| Persistence | Spring Data JPA, Hibernate |
| Database | MySQL for local runtime, H2 for automated tests |
| Frontend | Angular 20 |
| Build Tools | Maven Wrapper, Angular CLI |

## Current Features

- Monorepo structure with separate `backend` and `frontend` applications.
- Secure backend configuration that keeps local database credentials and JWT secrets out of version control.
- Product persistence with JPA entity validation and unique SKU enforcement.
- Database seed loader for 10 electronics products.
- Database-backed user account seeding from local configuration.
- BCrypt password hashing.
- JWT token generation and validation.
- Stateless Spring Security configuration.
- Public login endpoint.
- Protected Product CRUD REST API.
- CORS configuration for the Angular development server.
- Automated backend tests using H2.
- Angular application shell with protected routing.
- Angular login page connected to the backend JWT endpoint.
- Client-side JWT storage and automatic authorization header support.

## Project Structure

```text
.
├── backend
│   ├── pom.xml
│   ├── mvnw
│   └── src
│       ├── main
│       │   ├── java
│       │   └── resources
│       └── test
│           ├── java
│           └── resources
├── frontend
│   ├── angular.json
│   ├── package.json
│   └── src
└── README.md
```

## Backend API

The backend runs with the context path `/api`.

### Authentication

| Method | Endpoint | Access | Description |
| --- | --- | --- | --- |
| `POST` | `/api/auth/login` | Public | Authenticates a user and returns a JWT bearer token. |

### Products

All product endpoints require a valid JWT bearer token.

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/api/products` | Returns all products sorted by name. |
| `GET` | `/api/products/{id}` | Returns a product by ID. |
| `POST` | `/api/products` | Creates a product. |
| `PUT` | `/api/products/{id}` | Updates a product. |
| `DELETE` | `/api/products/{id}` | Deletes a product. |

## Local Configuration

Recommended local tooling:

- Java 25
- Node.js 20 LTS or newer LTS
- MySQL 8

The backend reads local-only settings from:

```text
backend/src/main/resources/application-local.properties
```

This file is intentionally ignored by git. Create it from the provided example:

```text
backend/src/main/resources/application-local.properties.example
```

Required local values:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/product?createDatabaseIfNotExist=false&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=your-mysql-username
spring.datasource.password=your-mysql-password
app.jwt.secret=replace-with-at-least-32-random-characters
app.seed.user.email=demo-user@example.com
app.seed.user.password=replace-with-local-demo-password
```

The seeded user credentials are local environment data and should not be committed.

## Running the Backend

Start MySQL and ensure the `product` database exists.

From the backend folder:

```bash
cd backend
./mvnw spring-boot:run
```

When the application starts, Hibernate updates the schema and the data loader inserts seed records if the tables are empty.

Backend base URL:

```text
http://localhost:8080/api
```

## Running Backend Tests

The backend test suite uses an in-memory H2 database, so it does not require MySQL.

```bash
cd backend
./mvnw test
```

Current test coverage verifies:

- Spring application context startup.
- Product seed data loading.
- Seeded user creation with hashed password.
- JWT generation and decoding.
- Login endpoint response.
- Unauthorized access protection.
- Authenticated Product list, create, update, delete, not-found, and duplicate SKU flows.

## Running the Frontend

The Angular frontend currently includes the application shell, login route, authenticated route guard, and an initial protected product workspace.

From the frontend folder:

```bash
cd frontend
npm install
npm start
```

Frontend URL:

```text
http://localhost:4200
```

The frontend expects the backend API to be available at:

```text
http://localhost:8080/api
```

## Running Frontend Checks

```bash
cd frontend
npm run build
npx ng test --watch=false --browsers=ChromeHeadless
```

## Upcoming Frontend Work

Planned frontend work:

- Product list view.
- Product create and edit forms.
- Delete workflow.
- Professional responsive styling.

## Security Notes

- Local database credentials are excluded from version control.
- JWT signing secrets are excluded from version control.
- Passwords are stored as BCrypt hashes.
- Product endpoints require JWT authentication.
- Tests use isolated test configuration and do not rely on local runtime credentials.

## Development Milestones

- Initialized the monorepo structure.
- Flattened backend and frontend project folders.
- Added Product persistence and sample data.
- Added JWT authentication and seeded user support.
- Added secured Product CRUD API.
- Added Angular authentication shell and login flow.
- Next: build the Angular product management screens.
