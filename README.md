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
- ✅ Proper cache eviction strategy

---

## 🏗️ Architecture

Layered architecture:

Controller → DTO → Service → Repository → Database

Caching layer:

- `flagMetadata` → caches FeatureFlag entity
- `flagEvaluation` → caches evaluation result per `(flagId:userId:isAdmin)`

---

## ⚙️ Tech Stack

- Java 17+
- Spring Boot
- Spring Data JPA
- Spring Validation
- Spring Cache
- H2 / PostgreSQL
- Lombok
- Maven

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

This ensures deterministic rollout per user.

---

## ⚡ Caching Strategy

To improve performance:

- Feature flag metadata is cached by ID
- Evaluation results are cached per `(flagId:userId:isAdmin)`
- Cache is evicted on:
    - Flag update
    - Flag toggle
    - Flag deletion

This reduces repeated database calls for high-frequency evaluation requests.

---

## ▶️ Running the Application

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

- Redis-based distributed caching
- Swagger/OpenAPI documentation
- Metrics and monitoring (Micrometer)
- Multi-tenant support
- A/B testing support
- Role-based authentication

---

## 🎯 Why This Project?

This project demonstrates:

- Clean layered architecture
- Business logic separation
- Deterministic rollout algorithm
- Performance optimization with caching
- Production-oriented backend thinking

---

## 👨‍💻 Author

Built as a backend engineering practice project to simulate real-world feature management systems.