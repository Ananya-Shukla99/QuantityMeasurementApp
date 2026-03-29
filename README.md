# Quantity Measurement App

## Overview

The Quantity Measurement App is a Java-based application designed to compare different physical quantities such as length and weight across various units.

The application is developed incrementally using clearly defined use cases. Each use case introduces a specific level of functionality, ensuring controlled scope, maintainability, and clean architecture.

The project emphasizes:
- Object-Oriented Design principles
- DRY (Don’t Repeat Yourself)
- Incremental development
- Clean code practices
- Proper unit testing

---  

## Objective

The primary goal of this application is to:

- Compare quantities across different units
- Support unit conversion
- Maintain correctness using test-driven development
- Gradually evolve toward supporting quantity arithmetic
- Demonstrate scalable design using enums and generic modeling

---

## Development Approach

This project follows a Use Case Driven Development approach.

Each use case:
- Has a clearly defined scope
- Introduces a small, testable feature
- Avoids unnecessary complexity
- Builds upon the previous implementation

Note:
The scope must remain limited to the current use case requirements.  
Over-engineering or prematurely adding features reduces maintainability and clarity, especially in enterprise environments where domain knowledge may not always be fully available.

---
### UC17 – Spring Backend for Quantity Measurement

- Migrates the backend of the Quantity Measurement Application to a **Spring-based architecture** for enterprise-grade scalability and maintainability  
- Introduces **Spring Boot** to simplify project setup, dependency management, application configuration, and runtime execution  
- Applies core **Spring concepts** such as **Inversion of Control (IoC)** and **Dependency Injection (DI)** to manage components cleanly  
- Builds RESTful APIs using **Spring MVC** and **Spring Controllers** for quantity operations such as comparison, conversion, and arithmetic  
- Moves business rules into dedicated **Spring Services**, keeping controllers lightweight and focused on request handling  
- Establishes a structured backend design using layered architecture with controllers, services, and repositories  
- Integrates persistence more cleanly through **Spring Data / JPA concepts** for improved database interaction and future ORM support  
- Uses **Spring Scopes** and managed beans to control object lifecycle where appropriate  
- Adds application-level **logging** for request tracing, debugging, and operational visibility  
- Improves maintainability, modularity, and testability through Spring’s ecosystem and annotation-driven development  
- Provides a scalable backend foundation for authentication, frontend integration, and future deployment  
