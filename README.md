# Multi-System Java Problem Statements

This repository contains various Java-based management and booking systems, each demonstrating core object-oriented principles and JUnit testing.

## Project Structure

The projects within this repository follow a standard Maven/Gradle-like directory structure. Each problem statement's codebase is located under a logical package hierarchy.


## Projects

* **Online Bookstore System**
    * Manages books, users, and orders for an online bookstore.
    * Supports Browse, cart management, and order placement.
    * Includes robust JUnit test coverage.

* **Employee Management System**
    * Handles employee records, departments, and roles within an organization.
    * Allows adding, updating, removing employees, and managing organizational structure.
    * Features comprehensive JUnit test suites.

* **Banking Transaction System**
    * Simulates account management and various financial transactions.
    * Enables deposits, withdrawals, and transfers between accounts.
    * Comes with extensive JUnit test cases for all operations.

* **Movie Ticket Booking System**
    * Facilitates booking movie tickets, managing showtimes, and seat availability.
    * Supports Browse movies, seat selection, and booking confirmation.
    * Validated by a suite of JUnit tests.

* **Task Scheduler**
    * Provides tools for creating, managing, and tracking tasks.
    * Allows task assignment, priority setting, and status updates.
    * Includes JUnit tests for core scheduling functionalities.

* **E-Commerce Order Processing**
    * Manages products, shopping carts, and automates order processing.
    * Features catalog management, cart operations, and order fulfillment logic.
    * Thoroughly tested with JUnit.

* **Online Course Management**
    * System for managing courses, student enrollments, and instructor assignments.
    * Supports course administration, student registration, and progress tracking.
    * Equipped with JUnit tests for reliability.

* **Feedback Aggregator System**
    * Collects, categorizes, and analyzes user feedback from various sources.
    * Enables feedback submission, categorization, and reporting.
    * Ensures data integrity through JUnit testing.

* **Smart Car Rental Management System**
    * Handles car inventory, customer data, and rental/reservation processes.
    * Allows car booking, customer management, and rental tracking.
    * Includes JUnit tests for all rental operations.

## Dependencies

This project primarily utilizes the following dependencies:

* **JUnit 5 (Jupiter API & Engine)**: Used for writing and running unit tests across all included systems.
```xml
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-api</artifactId>
            <version>5.13.0-RC1</version>
            <scope>test</scope>
        </dependency>