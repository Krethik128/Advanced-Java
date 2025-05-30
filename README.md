# Multi-System Java Problem Statements

This repository contains various Java-based management and booking systems, each demonstrating core object-oriented principles and JUnit testing.

## Project Structure

The projects within this repository follow a standard Maven/Gradle-like directory structure. Each problem statement's codebase is located under a logical package hierarchy.


## Projects

* **Online Bookstore System**
    * Manages books, users, and orders for an online bookstore.
    * Supports Browse, cart management, and order placement.
    * Includes robust JUnit test coverage.
    *  Adheres to the Single Responsibility Principle (SRP) by ensuring each module has a single, clear purpose. Employs the Open/Closed Principle (OCP), particularly in its flexible discount strategy, allowing new discount types to be added without modifying existing code

* **Banking Transaction System**
    * Simulates account management and various financial transactions.
    * Enables deposits, withdrawals, and transfers between accounts.
    * Comes with extensive JUnit test cases for all operations.
    * Built with the Liskov Substitution Principle (LSP) at its core, our system allows for seamless extension of Accounts into specific types like SavingsAccount and CurrentAccount while maintaining their core functionality. This design, combined with the Open/Closed Principle (OCP), ensures you can easily add new methods or services to BankingServices without ever needing to modify existing, proven code. 

* **Movie Ticket Booking System**
    * Facilitates booking movie tickets, managing showtimes, and seat availability.
    * Supports Browse movies, seat selection, and booking confirmation.
    * Validated by a suite of JUnit tests.
    * Designed using the Single Responsibility Principle (SRP), separating concerns like movie management, seat allocation, and booking processes into distinct modules. It also adheres to the Open/Closed Principle (OCP), allowing new payment methods or notification types to be integrated seamlessly.

* **E-Commerce Order Processing**
    * Manages products, shopping carts, and automates order processing.
    * Features catalog management, cart operations, and order fulfillment logic.
    * Thoroughly tested with JUnit.
    * Implements the Interface Segregation Principle (ISP) by providing a NotificationService interface which allows creating different kinds of notifications like sms,email, etc. It also leverages the Open/Closed

* **Smart Car Rental Management System**
    * Handles car inventory, customer data, and rental/reservation processes.
    * Allows car booking, customer management, and rental tracking.
    * Includes JUnit tests for all rental operations.
    * esigned for extensibility using the Open/Closed Principle (OCP), allowing easy addition of new vehicle types or pricing models without altering existing code. It also applies the Single Responsibility Principle (SRP) by separating concerns, ensuring booking logic, vehicle management, and pricing each reside in their own dedicated classes.

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