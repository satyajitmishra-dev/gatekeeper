# Gatekeeper

Gatekeeper is a distributed, high-performance API Gateway and Management ecosystem built on Java 25 and Spring Boot. It provides route orchestration, policy enforcement, centralized configuration, and real-time analytics.

---

## Architecture Overview

Gatekeeper is organized into three microservices:

```
gatekeeper/
├── smart-gateway-service/     # Core API Gateway (Routing, Security, Rate Limiting, Proxying)
├── api-management-service/    # Control Plane (Route Configurations, Admin API, Schema Management)
└── analytics-service/         # Telemetry & Observability (Metrics, Request Analytics, MongoDB Logging)
```

### Services Breakdown

| Service | Port | Description | Tech Stack |
| :--- | :---: | :--- | :--- |
| **`smart-gateway-service`** | `8080` | High-throughput gateway handling client requests, authentication, and forwarding. | Spring Boot 4.x, Redis, Kafka, OAuth2 Resource Server |
| **`api-management-service`** | `8081` | Centralized route configuration and administrative management. | Spring Boot 4.x, MySQL, Flyway, Spring Data JPA, Kafka |
| **`analytics-service`** | `8082` | Real-time traffic analytics, latency tracking, and metric ingestion. | Spring Boot 4.x, MongoDB, Spring Data JPA, Kafka |

---

## Prerequisites

- **Java**: JDK 25
- **Maven**: 3.9+ (or included `./mvnw` wrapper in each service)
- **Infrastructure**:
  - MySQL 8.x
  - Redis
  - Apache Kafka
  - MongoDB

---

## Getting Started

### 1. Clone the Repository
```bash
git clone https://github.com/satyajitmishra-dev/gatekeeper.git
cd gatekeeper
```

### 2. Configure Environment & Properties
Copy the sample configuration file in each service to set up your local environment:
```bash
cp analytics-service/src/main/resources/application.properties.example analytics-service/src/main/resources/application.properties
cp api-management-service/src/main/resources/application.properties.example api-management-service/src/main/resources/application.properties
cp smart-gateway-service/src/main/resources/application.properties.example smart-gateway-service/src/main/resources/application.properties
```
Update your database, Redis, and Kafka settings in the respective `application.properties` files.

### 3. Build the Services
You can build each service individually using Maven:

```bash
# Build API Management Service
cd api-management-service
./mvnw clean package

# Build Smart Gateway Service
cd ../smart-gateway-service
./mvnw clean package

# Build Analytics Service
cd ../analytics-service
./mvnw clean package
```

### 4. Run the Services
Run each service in separate terminals or your preferred IDE:
```bash
./mvnw spring-boot:run
```

---

## License

This project is licensed under the terms specified by the author.
