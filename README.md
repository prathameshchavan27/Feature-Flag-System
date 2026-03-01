# 🚀 Feature Flag System

A production-style Feature Flag system built with Spring Boot.

This service allows dynamic feature rollout, per-user overrides, and percentage-based gradual release — similar to how modern platforms control feature deployments.

---

## 📌 Features

- ✅ Create, update, delete feature flags
- ✅ Percentage-based rollout (0–100%)
- ✅ Admin-only flags
- ✅ Per-user overrides
- ✅ Flag evaluation API
- ✅ DTO-based clean architecture
- ✅ Request validation
- ✅ Global exception handling
- ✅ Spring Cache integration
- ✅ Redis distributed caching
- ✅ Proper cache eviction strategy
- ✅ Actuator monitoring support
- ✅ Dockerized deployment ready

---

## 🏗️ Architecture

Layered architecture:

Controller → DTO → Service → Repository → Database

Caching layer:

- `flagMetadata` → caches FeatureFlag entity
- `flagEvaluation` → caches evaluation result per `(flagId:userId:isAdmin)`

---

## 🧠 How Evaluation Works

1. If flag is disabled → return `false`
2. If user override exists → return override value
3. If `adminOnly = true` and user is not admin → return `false`
4. If rollout = 100 → return `true`
5. If rollout = 0 → return `false`
6. Otherwise:

```
bucket = abs(userId.hashCode()) % 100
return bucket < rolloutPercentage
```

This ensures deterministic rollout per user and horizontal scalability.

---

## ⚡ Redis Architecture Explanation

In production systems, applications typically run multiple instances behind a load balancer.

Local in-memory caching is not shared across instances, which can cause:

- Inconsistent flag evaluations
- Increased database load
- Poor scalability

To solve this, Redis is used as a distributed cache.

### Architecture Flow

Client  
↓  
Spring Boot Application  
↓  
Spring Cache Abstraction  
↓  
Redis (Distributed Cache)  
↓  
PostgreSQL

### Request Flow

1. Application checks Redis cache
2. If cache hit → return instantly
3. If cache miss → fetch from DB → compute → store in Redis → return

This dramatically reduces database load for high-frequency evaluation requests.

---

## ⚡ Caching Strategy Explanation

Two cache regions are used:

### 1️⃣ flagMetadata

- Key: `flagId`
- Value: FeatureFlag entity
- Purpose: Avoid repeated database lookups for flag configuration

### 2️⃣ flagEvaluation

- Key: `flagId:userId:isAdmin`
- Value: Boolean
- Purpose: Avoid recomputing rollout logic repeatedly

### Cache Eviction

Cache is evicted when:

- Flag is updated
- Flag is toggled
- Flag is deleted
- Override is created / updated / deleted

This ensures cache consistency with the database.

---

## ⏳ TTL (Time-To-Live) Reasoning

TTL is configured to automatically expire cache entries.

Example strategy:

- `flagMetadata` → Longer TTL (e.g., 10 minutes)
- `flagEvaluation` → Shorter TTL (e.g., 5 minutes)

### Why TTL?

- Prevents stale distributed cache
- Provides safety if eviction fails
- Controls Redis memory usage
- Ensures periodic refresh of cached data

Evaluation results are more dynamic → shorter TTL  
Metadata changes less frequently → longer TTL

---

## 📊 Actuator & Metrics

Spring Boot Actuator is integrated for monitoring and observability.

### Useful Endpoints

- `/actuator/health` → Application health
- `/actuator/metrics` → Performance metrics
- `/actuator/caches` → Cache statistics
- `/actuator/prometheus` → Prometheus integration

### Metrics You Can Monitor

- Cache hit/miss ratio
- HTTP request count
- Response time metrics
- JVM memory usage
- Thread usage

This makes the service production-ready for monitoring tools like Prometheus and Grafana.

---

## ⚙️ Tech Stack

- Java 17+
- Spring Boot
- Spring Data JPA
- Spring Validation
- Spring Cache
- Redis
- PostgreSQL
- Spring Boot Actuator
- Micrometer
- Lombok
- Maven
- Docker

---

## 📂 Project Structure

```
com.featureflag
├── controller
├── service
├── repository
├── dto
├── pojos
├── exception
├── config
```

---

## 🔌 API Endpoints

### 🟢 Feature Flags

| Method | Endpoint | Description |
|--------|----------|------------|
| POST   | `/flags` | Create new flag |
| GET    | `/flags` | Get all flags |
| GET    | `/flags/{id}` | Get flag by id |
| PUT    | `/flags/{id}` | Update flag |
| DELETE | `/flags/{id}` | Delete flag |
| PATCH  | `/flags/{id}/toggle` | Toggle enable/disable |
| GET    | `/flags/{id}/enabled?userId=10&isAdmin=false` | Evaluate flag |

---

### 🟠 User Overrides

| Method | Endpoint | Description |
|--------|----------|------------|
| POST   | `/flags/{id}/override` | Create/update override |
| DELETE | `/flags/{id}/override/{userId}` | Delete override |

---

## ▶️ Running the Application (Without Docker)

### 1️⃣ Clone the repository

```
git clone <your-repo-url>
```

### 2️⃣ Build

```
mvn clean install
```

### 3️⃣ Run

```
mvn spring-boot:run
```

Application runs on:

```
http://localhost:8080
```

---

## 🐳 Docker Setup Instructions

### 1️⃣ Dockerfile

```dockerfile
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/feature-flag.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
```

### 2️⃣ docker-compose.yml

```yaml
version: '3.8'

services:
  app:
    build: .
    ports:
      - "8080:8080"
    depends_on:
      - redis
      - postgres

  redis:
    image: redis:7
    ports:
      - "6379:6379"

  postgres:
    image: postgres:15
    environment:
      POSTGRES_DB: featureflag
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: password
    ports:
      - "5432:5432"
```

### 3️⃣ Run with Docker

```
docker-compose up --build
```

Application runs at:

```
http://localhost:8080
```

---

## 🧪 Example Flow

### Step 1 — Create Flag

```
POST /flags
```

```json
{
  "name": "new_checkout",
  "enabled": true,
  "adminOnly": false,
  "rolloutPercentage": 30
}
```

### Step 2 — Evaluate

```
GET /flags/1/enabled?userId=10&isAdmin=false
```

Response:

```
true / false
```

### Step 3 — Add Override

```
POST /flags/1/override
```

```json
{
  "userId": 90,
  "enabled": true
}
```

Now user 90 always gets access regardless of rollout.

---

## 📈 Future Improvements

- Swagger/OpenAPI documentation
- Multi-tenant support
- A/B testing support
- Role-based authentication
- Admin dashboard UI

---

## 🎯 Why This Project?

This project demonstrates:

- Clean layered architecture
- Business logic separation
- Deterministic rollout algorithm
- Distributed caching with Redis
- Proper cache invalidation strategies
- Observability integration
- Production-oriented backend thinking

---

## 👨‍💻 Author

Built as a backend engineering practice project to simulate real-world feature management systems.