# Platform for Cats and Owners

This project is a platform for cats and their owners, providing tools for interaction and management. The project is implemented in three stages, showcasing a progression in architectural complexity and adoption of modern frameworks.

---

## Table of Contents
- [About the Project](#about-the-project)
- [Technologies Used](#technologies-used)
- [Project Structure](#project-structure)
- [Setup and Installation](#setup-and-installation)
- [Usage](#usage)
- [Development Stages](#development-stages)
- [License](#license)

---

## About the Project
The platform enables:
- Management of cats and their owners.
- Interaction between users via REST APIs.
- Secure access to resources through authentication and authorization mechanisms.

---

## Technologies Used
- **Java 17**
- **Spring Framework**:
  - Spring Boot
  - Spring Web Starter
  - Spring Data JPA
  - Spring Security
- **Hibernate ORM**
- **H2 Database** (for testing purposes; can be replaced with other databases like MySQL or PostgreSQL)
- **JWT Authentication**
- **Gradle** (build tool)

---

## Project Structure
The project is structured into three main layers:

1. **Controller**: Handles HTTP requests and responses.
2. **Service**: Contains business logic.
3. **DAO (Data Access Object)**: Handles database interactions using Hibernate ORM (Stage 1) and Spring Data JPA (Stage 2 and 3).

---

## Setup and Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/Clawf1/Pet_project
   cd v1 # /v2/v3
   ```

2. **Build the project:**
   ```bash
   ./gradlew clean build
   ```

3. **Run the application:**
   ```bash
   ./gradlew bootRun
   ```

4. **Access the application:**
   - Main endpoint: `http://localhost:8080`

---

## Usage

### Example API Endpoints:
- **GET /api/cats**: Retrieve a list of cats.
- **POST /api/owners**: Add a new owner.
- **PUT /api/cats/{id}**: Update details of a specific cat.
- **DELETE /api/owners/{id}**: Delete an owner.

### Authentication:
- Obtain a JWT token by sending valid credentials to `/api/auth/login`.
- Use the token in the `Authorization` header for subsequent requests: `Bearer <token>`.

---

## Development Stages

### Stage 1: Basic Architecture
- Implemented a 3-layer architecture:
  - **Controller**: Handles incoming HTTP requests.
  - **Service**: Business logic.
  - **DAO**: Database access using Hibernate ORM.
- Features:
  - CRUD operations for cats and owners.

### Stage 2: Spring Integration
- Replaced Hibernate ORM with **Spring Data JPA**.
- Added **Spring Dependency Injection (DI)**.
- Introduced **RESTful APIs** with `@RestController`.
- Dependencies:
  - `spring-boot-starter-web`
  - `spring-boot-starter-data-jpa`

### Stage 3: Security Enhancements
- Added **Spring Security**.
- Implemented **JWT-based authentication**:
  - Stateless requests.
  - Token-based access control.
- Secured API endpoints:
  - Public endpoints for registration and login.
  - Protected endpoints for CRUD operations.

---

## Contribution
Feel free to contribute by creating pull requests or reporting issues in the GitHub repository.

