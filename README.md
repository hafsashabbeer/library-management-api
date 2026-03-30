# Library Management API

A Spring Boot REST API for managing books, members, and borrowing operations in a library system.

---

##  Features

- 📖 Book Management (CRUD)
- 👤 Member Management (CRUD)
- 🔄 Borrow & Return Books
- ❌ Prevent borrowing unavailable books
- ❌ Prevent deleting members with active borrows
- 📦 DTO-based clean API responses
- ⚠️ Global exception handling
- 📄 Pagination support for books and members
- 🧪 Unit testing with JUnit & Mockito

---

##  Tech Stack

- Java 17+
- Spring Boot
- Spring Data JPA (Hibernate)
- MySQL
- Maven
- JUnit 5 & Mockito

---

##  Project Structure

```text
src/main/java/com/library/api
├── controller    # REST Controllers
├── service       # Business Logic
├── repository    # JPA Repositories
├── model         # Entity Classes
├── dto           # Request/Response DTOs
├── exception     # Custom Exceptions & Handler