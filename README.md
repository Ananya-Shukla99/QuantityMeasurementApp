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
## UC2: Feet and Inches Measurement Equality

## Description
Extends UC1 to support equality checks for both Feet and Inches independently using separate classes. Reduces main method dependency via static helper methods.

## Flow
1. Static method validates two feet values → compares equality.
2. Static method validates two inches values → compares equality.
3. Returns `true` / `false` for each comparison.

## Key Concepts
- Separate `Inches` class mirroring `Feet` (same equality logic)
- Static methods for Feet and Inches equality checks
- Violates DRY principle (addressed in UC3)

---
