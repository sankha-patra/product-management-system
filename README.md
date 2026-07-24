# Product Management System

Full-stack application built with **Spring Boot 3.2**, **Angular 17**, and **PostgreSQL**.

## Features

- **JWT Authentication** — Register/Login with encrypted passwords (BCrypt)
- **Category CRUD** — Create, Read, Update, Delete categories
- **Product CRUD** — Products linked to categories with full CRUD
- **Server-side Pagination** — Page, sort (by price), search, category filter
- **Bulk Upload** — Async CSV upload with batch processing (500 per batch) — no 504 timeouts
- **Report Generation** — Streaming CSV/XLSX download — no 504 timeouts
- **Premium Dark UI** — Glassmorphism, gradients, animations

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Spring Boot 3.2.4, Spring Security, Spring Data JPA |
| Frontend | Angular 17, Angular Material |
| Database | PostgreSQL |
| Auth | JWT (jjwt 0.12.5) |
| Reports | Apache POI (XLSX), OpenCSV (CSV) |

## Prerequisites

- Java 17+
- Node.js 18+
- PostgreSQL 15+
- Maven 3.8+

## Setup

### 1. Database

```sql
CREATE DATABASE productmgmt;
```

Default credentials in `application.properties`:
- URL: `jdbc:postgresql://localhost:5432/productmgmt`
- Username: `postgres`
- Password: `postgres`

### 2. Backend

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

Backend runs at: **http://localhost:8080**

### 3. Frontend

```bash
cd frontend
npm install
npm start
```

Frontend runs at: **http://localhost:4200**

### 4. Run Tests

```bash
cd backend
mvn test
```

Tests use H2 in-memory database (no PostgreSQL needed).

## API Endpoints

### Auth (No token required)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register with email/password |
| POST | `/api/auth/login` | Login, returns JWT token |

### Categories (JWT required)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/categories` | List all categories |
| GET | `/api/categories/{id}` | Get category by ID |
| POST | `/api/categories` | Create category |
| PUT | `/api/categories/{id}` | Update category |
| DELETE | `/api/categories/{id}` | Delete category |

### Products (JWT required)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/products` | List with pagination/sort/search |
| GET | `/api/products/{id}` | Get product by ID |
| POST | `/api/products` | Create product |
| PUT | `/api/products/{id}` | Update product |
| DELETE | `/api/products/{id}` | Delete product |
| POST | `/api/products/bulk-upload` | Upload CSV file (async) |
| GET | `/api/products/report?format=csv` | Download CSV report |
| GET | `/api/products/report?format=xlsx` | Download XLSX report |

### Pagination Parameters
| Param | Default | Description |
|-------|---------|-------------|
| `page` | 0 | Page number (0-indexed) |
| `size` | 10 | Page size |
| `sortBy` | price | Sort field |
| `sortDir` | asc | Sort direction (asc/desc) |
| `search` | — | Search by product name |
| `categoryId` | — | Filter by category |

## Bulk Upload CSV Format

```csv
name,image,price,categoryId
Laptop,https://example.com/laptop.jpg,999.99,1
Phone,https://example.com/phone.jpg,699.99,1
```

## Postman Collection

Import `postman/ProductMgmt.postman_collection.json` into Postman.

The collection auto-saves tokens and IDs between requests. Run requests in order:
1. Register → 2. Login → 3. Create Category → 4. Create Product → ...

## Project Structure

```
product-management-system/
├── backend/
│   ├── pom.xml
│   └── src/main/java/com/productmgmt/
│       ├── ProductMgmtApplication.java
│       ├── config/        (SecurityConfig, JwtUtil, JwtAuthFilter, AsyncConfig)
│       ├── controller/    (AuthController, CategoryController, ProductController)
│       ├── dto/           (AuthRequest/Response, CategoryDTO, ProductDTO, etc.)
│       ├── entity/        (User, Category, Product)
│       ├── exception/     (GlobalExceptionHandler, ResourceNotFoundException)
│       ├── repository/    (UserRepository, CategoryRepository, ProductRepository)
│       └── service/       (AuthService, CategoryService, ProductService, ReportService)
├── frontend/
│   ├── package.json
│   └── src/app/
│       ├── components/    (login, register, layout, dashboard, categories, products)
│       ├── services/      (auth, category, product)
│       ├── interceptors/  (auth interceptor)
│       └── guards/        (auth guard)
└── postman/
    └── ProductMgmt.postman_collection.json
```
