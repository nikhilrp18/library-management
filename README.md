# SpringBooks Library Management API

A RESTful API for managing a community library system built with Spring Boot, featuring book and member management with secure borrowing and returning operations.

## Features

- **Book Management**: Create, read, update, and delete books
- **Member Management**: Register and manage library members
- **Library Operations**: Secure book borrowing and returning with business rules
- **JWT Security**: All endpoints secured with OAuth 2.0 / JWT authentication
- **H2 Database**: In-memory database for development and testing
- **Comprehensive Testing**: Unit and integration tests with security testing
- **API Documentation**: Interactive Swagger/OpenAPI documentation

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+ (or use the included Maven wrapper)

### Building and Running

1. **Clone the repository**
   ```powershell
   git clone <repository-url>
   cd library-management
   ```

2. **Build the application**
   ```powershell
   mvn clean compile
   ```

3. **Run tests**
   ```powershell
   mvn test
   ```

4. **Start the application**
   ```powershell
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

### Database Access

- **H2 Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:librarydb`
  - Username: `sa`
  - Password: `password`

### API Documentation

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI Spec**: http://localhost:8080/v3/api-docs

## API Endpoints

### Authentication

All API endpoints require a valid JWT token. Include the token in the Authorization header:

```
Authorization: Bearer <your-jwt-token>
```

### Book Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/books` | Add a new book |
| GET | `/api/books` | Get all books |
| GET | `/api/books/{id}` | Get book by ID |
| PUT | `/api/books/{id}` | Update book |
| DELETE | `/api/books/{id}` | Delete book |

### Member Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/members` | Register a new member |
| GET | `/api/members` | Get all members |
| GET | `/api/members/{id}` | Get member by ID |
| PUT | `/api/members/{id}` | Update member |

### Library Operations

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/borrow/{bookId}/member/{memberId}` | Borrow a book |
| POST | `/api/return/{bookId}` | Return a book |

## API Usage Examples

### 1. Create a Book

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/books" `
  -Method POST `
  -Headers @{"Authorization" = "Bearer <your-jwt-token>"; "Content-Type" = "application/json"} `
  -Body '{
    "title": "The Great Gatsby",
    "author": "F. Scott Fitzgerald",
    "isbn": "9780743273565"
  }'
```

### 2. Register a Member

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/members" `
  -Method POST `
  -Headers @{"Authorization" = "Bearer <your-jwt-token>"; "Content-Type" = "application/json"} `
  -Body '{
    "name": "John Doe",
    "email": "john.doe@example.com"
  }'
```

### 3. Borrow a Book

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/borrow/1/member/1" `
  -Method POST `
  -Headers @{"Authorization" = "Bearer <your-jwt-token>"}
```

### 4. Return a Book

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/return/1" `
  -Method POST `
  -Headers @{"Authorization" = "Bearer <your-jwt-token>"}
```

### 5. Get All Books

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/books" `
  -Method GET `
  -Headers @{"Authorization" = "Bearer <your-jwt-token>"}
```

## JWT Token for Testing

For testing purposes, you can use any JWT token structure. The application is configured with a mock JWT decoder that accepts any valid JWT format.

Example test token

```
eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0LXVzZXIiLCJhdWQiOiJzcHJpbmdib29rcy1hcGkiLCJpc3MiOiJodHRwczovL2Rldi1leGFtcGxlLmF1dGgwLmNvbS8iLCJleHAiOjk5OTk5OTk5OTksImlhdCI6MTYzMDAwMDAwMCwic2NvcGUiOiJyZWFkIHdyaXRlIn0.example-signature
```

## Business Rules

### Book Borrowing Rules

1. A book cannot be borrowed if it is already borrowed (`isBorrowed` is `true`)
2. A member cannot borrow a book if they do not exist
3. A book cannot be borrowed if it does not exist
4. If successful, the book's `isBorrowed` status is set to `true`

### Book Returning Rules

1. A book cannot be returned if it is not currently borrowed
2. A book cannot be returned if it does not exist
3. If successful, the book's `isBorrowed` status is set to `false`

## Error Handling

The API provides consistent error responses with appropriate HTTP status codes:

- **400 Bad Request**: Invalid input or business rule violation
- **401 Unauthorized**: Missing or invalid JWT token
- **404 Not Found**: Resource not found
- **409 Conflict**: Duplicate resource (ISBN/email already exists) or book already borrowed
- **500 Internal Server Error**: Unexpected server error

## Testing

Run the test suite:

```powershell
# Run all tests
mvn test

The test suite includes:
- Unit tests for service layer business logic
- Integration tests for REST controllers with security
- Mock JWT authentication for testing secured endpoints
