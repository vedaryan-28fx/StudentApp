# Student, Course & Book Management API

![Java](https://img.shields.io/badge/Java-26-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.x-6DB33F?style=flat-square&logo=springboot&logoColor=white)
![Spring AOP](https://img.shields.io/badge/Spring-AOP-6DB33F?style=flat-square&logo=spring&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Docker-4169E1?style=flat-square&logo=postgresql&logoColor=white)

A containerized, layered RESTful backend built with **Java 26**, **Spring Boot 4**, **Spring Data JPA (Hibernate)**, **Spring AOP**, and **PostgreSQL**, managing `1:N` (`Student -> Book`) and `M:N` (`Student <-> Course`) domain workflows with environment-isolated profiles (`dev` / `prod`).

---

## Architecture & Request Flow

```mermaid
flowchart TB
    Client([REST Client / Postman]) -->|HTTP / JSON| Controllers

    subgraph Web_Layer [Web MVC Layer - Java Record DTOs]
        Controllers[StudentController · CourseController · BookController]
    end

    subgraph AOP_Proxy [Spring Core & AOP Cross-Cutting Layer]
        Aspect[ServiceExecutionLoggingAspect<br/>@Around @LogExecutionTime · @AfterThrowing]
    end

    subgraph Service_Layer [Transactional Domain Layer - Constructor DI]
        Services[StudentService · CourseService · BookService<br/>@Transactional + Bidirectional Sync]
    end

    subgraph Persistence_Layer [Spring Data JPA + HikariCP]
        Repos[StudentRepository · CourseRepository · BookRepository]
        Seeder[Populate CommandLineRunner<br/>@Profile 'dev' only]
    end

    subgraph DB_Layer [Dockerized PostgreSQL]
        T_Student[(students)]
        T_Book[(books · FK student_id)]
        T_Join[(student_courses · PK student_id, course_id)]
        T_Course[(courses)]

        T_Student -->|1:N orphanRemoval=true| T_Book
        T_Student -->|M:N Set| T_Join
        T_Join -->|M:N| T_Course
    end

    Controllers --> Aspect
    Aspect --> Services
    Services --> Repos
    Seeder -.->|dev profile| Repos
    Repos --> DB_Layer
```

---

## Database Schema (ER Diagram)

Three JPA entities (`Student`, `Book`, `Course`) map onto four PostgreSQL tables (`students`, `books`, `courses`, and the `student_courses` join table) with `students` at the center (`1:N` ownership to `books`, `M:N` enrollment with `courses`).

![Database ER Diagram](docs/er-diagram.svg)

---

## Core Engineering Features

- **Strict Layered Architecture & Zero Entity Leakage:** `Controller -> Service -> Repository` using constructor-injected `final` dependencies, immutable Java `record` DTOs, and explicit `ResponseEntity` status semantics (`201`, `200`, `204`, `404`).
- **Relational Integrity (`1:N` & `M:N`):** Bidirectional `Student <-> Book` ownership with `orphanRemoval = true` and `Set`-backed `@ManyToMany` course enrollment (`student_courses`) to prevent join-table wipe-and-reinsert overhead.
- **AOP Execution Profiling:** Custom `@LogExecutionTime` annotation and `ServiceExecutionLoggingAspect` (`@Around` / `@AfterThrowing`) logging service latency in milliseconds and exceptions across domain boundaries.
- **Profile-Isolated Startup & Seeding:** Separate `application-dev.yml` and `application-prod.yml` configurations with `Populate` `CommandLineRunner` gated strictly to `@Profile("dev")`.

---

## Local Setup

```bash
# 1. Start PostgreSQL container
docker compose up -d

# 2. Run with dev profile (seeds sample data)
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```