# 🩺 Healthcare Management

A pet project for managing user authentication with JWT using Spring Boot 3.

## 🚀 Tech Stack

- Java 21
- Spring Boot 3
- Vue JS 3
- Docker
- Testcontainers


---
## 🗂️ Project Structure

```
healthcare-management
├── frontend
├── scripts
├── k8s
├── terraform
├── src
│   └── main
│       ├── java
│       └── resources
│           └── application.yml
├── docker-compose.yml
├── Dockerfile
├── README.md
└── pom.xml
```
---
## 🐳 Docker Usage

- Build the Docker images:
```bash
   docker build -t postgres .
   docker build -t healthcare-frontend .
   docker build -t healthcare-backend .
```

- Run the containers:
```bash
   docker run -p 5432:5432 postgres
   docker run -p 3000:3000 healthcare-frontend
   docker run -p 8080:8080 healthcare-backend
```
- Run the docker compose:
```bash
 docker-compose up
 ````
---

📌 License
This project is licensed under the MIT License.

---

## 🔐 Authentication Endpoints

### 📌 Registration
<img src="images/sign_up.png" alt="Registration Page" width="400px"/>

**Endpoints:**
- POST http://localhost:8080/api/v1/auth/register

**Request Body:**
```json
{
  "username": "hayk.hovhannisyan",
  "password": "hhovhann123"
}
```

**Response Body:**
 ```json
{
  "jwt": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoiaGhvdmhhbm4iLCJpYXQiOjE3NDkzNzcyOTgsImV4cCI6MTc0OTM4MDg5OH0.HUboOzOE59zAbMcwxjuP5XjXxoWawvhEiZtVSww0DTM",
  "username": "hhovhann",
  "message": "User registered successfully"
}

```

### 📌 Login
<img src="images/login.png" alt="Login Page" width="400px"/>

**Endpoints:**
- POST http://localhost:8080/api/v1/auth/login
  Authorization: Bearer <your_jwt_token_from__registration>

**Request Body:**
```json
{
  "username": "hayk.hovhannisyan",
  "password": "hhovhann123"
}
```

**Response Body:**
 ```json
{
  "jwt": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoiaGhvdmhhbm4iLCJpYXQiOjE3NDkzNzczNjcsImV4cCI6MTc0OTM4MDk2N30.uIQ8Ycq_ly9Sn3xrYlhZFMKm7dmOk82tGzWk7ptlcaI",
  "username": "hhovhann",
  "message": "Login successful"
}


```
### 📌 Public Page
<img src="images/public.png" alt="Public Page" width="600px"/>

### 📌 Dashboard Page
<img src="images/dashboard.png" alt="Dashboard Page" width="600px"/>

### 📌 Dashboard Chat AI Assistant
<img src="images/chat_assistant.png" alt="Dashboard Chat AI Assistant" width="600px"/>
