## Installation

### Prerequisites
- JDK 25
- Maven 3.9+
- PostgreSQL 16

### 1. Clone the repository

```bash
git clone <repository-url>
cd <project-folder>
```

### 2. Database Setup

Run:

```bash
psql -U postgres -f database/schema.sql
```



> **Schema:** `EventManagement`
### 3. Configure `application.properties`

Update the following properties:

Database properties
```properties
username: Username
password: Password
url: DB_URL
```
Jwt properties
```properties
jwt.secret=your-secret-key
```

### 4. Run the Application

```bash
mvn spring-boot:run
```

or

```bash
./mvnw spring-boot:run
```

### 5.Api Documentation
Swagger UI: `http://localhost:8080/swagger-ui/index.html`
