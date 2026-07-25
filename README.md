# Product Management System

Spring Boot + Angular + PostgreSQL

## Setup

### Database
```sql
CREATE DATABASE productmgmt;
```

### Backend
```bash
cd backend
mvn clean install -DskipTests
mvn spring-boot:run
```
Runs on http://localhost:8080

### Frontend
```bash
cd frontend
npm install
npm start
```
Runs on http://localhost:4200

### Default Config
- DB: `localhost:5432/productmgmt` (user: postgres, pass: postgres)
- JWT expires in 24 hours

## API

Auth: `POST /api/auth/register`, `POST /api/auth/login`

Categories: `GET/POST/PUT/DELETE /api/categories`

Products: `GET/POST/PUT/DELETE /api/products`
- Pagination: `?page=0&size=10&sortBy=price&sortDir=asc`
- Search: `?search=laptop`
- Filter: `?categoryId=1`

Bulk Upload: `POST /api/products/bulk-upload` (multipart CSV)

Reports: `GET /api/products/report?format=csv|xlsx`

## Postman
Import `postman/ProductMgmt.postman_collection.json` — run Register first, token auto-saves.

## CSV Format
```
name,image,price,categoryId
Laptop,https://example.com/img.jpg,999.99,1
```
