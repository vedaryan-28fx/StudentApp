# Unified Student, Course & Book Management API

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?style=flat-square&logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-4169E1?style=flat-square&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=flat-square&logo=docker&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-JPA-59666C?style=flat-square&logo=hibernate&logoColor=white)

A production-grade RESTful backend built with **Java**, **Spring Boot**, **Spring Data JPA (Hibernate)**, and **Dockerized PostgreSQL 16**. Designed around strict layered architecture (`Controller → Service → Repository`), immutable Java `record` DTO contracts, and write-optimized relational mappings (`1:N` and `M:N`).

---

## System Architecture

```mermaid
flowchart TB
    subgraph Client_Layer [HTTP / REST Clients]
        Postman([Postman / REST Client])
    end

    subgraph API_Layer [Web MVC Layer - Immutable Java Record DTOs]
        SC[StudentController<br/>/api/v1/students]
        CC[CourseController<br/>/api/v1/courses]
        BC[BookController<br/>/api/v1/books]
    end

    subgraph Service_Layer [Transactional Domain & Mapping Layer]
        SS[StudentService<br/>@Transactional + Sync Helpers]
        CS[CourseService<br/>@Transactional]
        BS[BookService<br/>@Transactional]
    end

    subgraph Persistence_Layer [Spring Data JPA + HikariCP Pool]
        SR[StudentRepository]
        CR[CourseRepository]
        BR[BookRepository]
    end

    subgraph Database_Layer [Dockerized PostgreSQL 16]
        DB[(PostgreSQL: student_db)]
        T_Student[Table: students]
        T_Book[Table: books<br/>FK: student_id]
        T_Join[Join Table: student_courses<br/>PK: student_id, course_id]
        T_Course[Table: courses]

        T_Student -->|1:N orphanRemoval=true| T_Book
        T_Student -->|M:N Set Semantics| T_Join
        T_Join -->|M:N| T_Course
    end

    Postman --> SC & CC & BC
    SC --> SS
    CC --> CS
    BC --> BS
    SS --> SR & CR & BR
    CS --> CR
    BS --> BR
    SR & CR & BR --> DB
```

---

## Relational Database Schema (ER Diagram)

Three JPA entities (`Student`, `Book`, `Course`) map cleanly onto four PostgreSQL tables with `Student` at the center (`Book *—1 Student *—* Course`):


![Database ER Diagram](docs/er-diagram.svg)


---

## Key Engineering Highlights

* **Zero Entity Leakage & N+1 Protection:** All endpoints accept and return immutable Java `record` DTOs mapped explicitly at the service boundary—avoiding reflection mappers (e.g., ModelMapper) that inadvertently trigger `FetchType.LAZY` proxy queries—with explicit `ResponseEntity` semantics (`201 Created`, `200 OK`, `204 No Content`, `404 Not Found`).
* **Bidirectional `1:N` Sync & Orphan Cleanup (`Student → Book`):** `Book` owns the `student_id` foreign key via `@ManyToOne`. Domain sync helpers (`addBook` / `removeBook`) atomically update both sides of the association inside `@Transactional`, allowing `orphanRemoval = true` to emit a targeted `DELETE` upon disassociation rather than leaving orphaned rows.
* **Write-Optimized `M:N` Set Semantics (`Student ↔ Course`):** Uses `Set<Course>` instead of `List<Course>` on the `@ManyToMany` join table (`student_courses`), enforcing a composite primary key `(student_id, course_id)`. This eliminates Hibernate's unindexed bag behavior (wiping all rows and re-inserting `N - 1` rows on a single unenrollment) and pairs with state-safe `equals()` / `hashCode()` to prevent broken `HashSet` buckets across entity lifecycle transitions.
* **Deterministic Container, Pool & Timezone Tuning:** Docker Compose pairs PostgreSQL 16 with a `pg_isready` healthcheck (`condition: service_healthy`) to prevent cold-start race conditions, bounded HikariCP settings (`maximum-pool-size: 10`, `connection-timeout: 20000`, `max-lifetime: 1800000`), and canonical timezone enforcement (`-Duser.timezone=Asia/Kolkata` + `hibernate.jdbc.time_zone=UTC`) to eliminate legacy `Asia/Calcutta` JDBC handshake crashes.

---

## API Reference

### Students (`/api/v1/students`)
| Method | Endpoint | Description | Status Codes |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/students` | Create a new student | `201 Created` |
| `GET` | `/api/v1/students` | Retrieve all students | `200 OK` |
| `GET` | `/api/v1/students/{id}` | Retrieve a student by ID | `200 OK`, `404 Not Found` |
| `PUT` | `/api/v1/students/{id}` | Update student details | `200 OK`, `404 Not Found` |
| `DELETE` | `/api/v1/students/{id}` | Delete a student (cascades owned books) | `204 No Content`, `404 Not Found` |

### Books & Ownership (`/api/v1/books` & `/api/v1/students/{studentId}/books`)
| Method | Endpoint | Description | Status Codes |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/books` | Create a standalone book resource | `201 Created` |
| `GET` | `/api/v1/books` | List all books | `200 OK` |
| `POST` | `/api/v1/students/{studentId}/books` | Create and assign a book directly to a student | `201 Created`, `404 Not Found` |
| `POST` | `/api/v1/students/{studentId}/books/{bookId}` | Assign an existing book to a student | `200 OK`, `404 Not Found` |
| `DELETE` | `/api/v1/students/{studentId}/books/{bookId}` | Disassociate and orphan-remove a book | `204 No Content`, `404 Not Found` |

### Courses & Enrollment (`/api/v1/courses` & `/api/v1/students/{studentId}/courses`)
| Method | Endpoint | Description | Status Codes |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/courses` | Create a new course | `201 Created` |
| `GET` | `/api/v1/courses` | List all courses | `200 OK` |
| `POST` | `/api/v1/students/{studentId}/courses/{courseId}` | Enroll a student in a course (`M:N`) | `200 OK`, `404 Not Found` |
| `DELETE` | `/api/v1/students/{studentId}/courses/{courseId}` | Unenroll a student (single-row join `DELETE`) | `204 No Content`, `404 Not Found` |

---

## Project Structure

```text
src/main/java/com/vedaryan/studentapp/
├── controller/       # REST controllers (Student, Course, Book)
├── service/          # @Transactional domain logic, sync helpers & DTO mapping
├── repository/       # Spring Data JPA repositories
├── entity/           # JPA entities (@Entity, @OneToMany, @ManyToMany)
├── dto/              # Immutable Java record request/response contracts
└── exception/        # Custom domain exceptions & global error handling
```

---

## Local Setup & Quickstart

### Prerequisites
* Java 21+ & Maven 3.9+
* Docker & Docker Compose

### 1. Clone the Repository
```bash
git https://github.com/vedaryan-28fx/StudentApp.git
cd StudentApp
```

### 2. Start PostgreSQL via Docker Compose
```bash
docker compose up -d
```
Verify the container reports `healthy` before starting the application:
```bash
docker compose ps
```

### 3. Run the Spring Boot Application
Pass the canonical JVM timezone flag so the PostgreSQL JDBC driver avoids legacy timezone handshake failures:
```bash
./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Duser.timezone=Asia/Kolkata"
```

The API will be available at `http://localhost:8080/api/v1`.

---

## Author

**Ved Aryan** — Backend Engineering (Java, Spring Boot, PostgreSQL, Microservices)  
[GitHub](https://github.com/vedaryan-28fx)
