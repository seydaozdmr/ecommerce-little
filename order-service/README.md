# Order Service

DDD-based microservice for order management with Kafka integration.

## Features

- Domain-Driven Design architecture
- Order creation with validation
- Kafka event publishing (OrderCreated event)
- In-memory data storage
- RESTful API with OpenAPI documentation

## Architecture

The project follows DDD principles with clear layer separation:

- **Domain Layer**: Core business logic (Order, OrderItem, OrderStatus, OrderCreated event)
- **Application Layer**: Use cases and DTOs (OrderService, CreateOrderRequest, OrderResponse)
- **Infrastructure Layer**: Technical implementations (Kafka, In-memory repository)
- **API Layer**: REST controllers and exception handling

## Prerequisites

- Java 17 (required - the project will not compile with other versions)
- Maven 3.6+
- Kafka (running on localhost:9092)

**Note**: If your system's default Java version is not 17, use the provided `mvnw17.sh` script instead of `mvn`:
```bash
./mvnw17.sh spring-boot:run
```

## Running the Application

```bash
# If Java 17 is your default:
mvn spring-boot:run

# If you have multiple Java versions:
./mvnw17.sh spring-boot:run
```

The application will start on `http://localhost:8080`

## API Documentation

Swagger UI: `http://localhost:8080/swagger-ui.html`

## API Endpoints

### Create Order
```bash
POST /api/orders
Content-Type: application/json

{
  "customerId": "CUST-001",
  "items": [
    {
      "productId": "PROD-001",
      "quantity": 2,
      "price": 99.99
    }
  ]
}
```

### Get Order by ID
```bash
GET /api/orders/{orderId}
```

### Get All Orders
```bash
GET /api/orders
```

### Get Orders by Customer ID
```bash
GET /api/orders/customer/{customerId}
```

## Kafka Integration

When an order is created, an `OrderCreated` event is published to the `order-events` topic.

Event structure:
```json
{
  "orderId": "uuid",
  "customerId": "CUST-001",
  "items": [...],
  "totalAmount": 199.98,
  "createdAt": "2025-11-26T16:17:00",
  "eventType": "ORDER_CREATED"
}
```

## Configuration

Key configuration in `application.yml`:
- Server port: 8080
- Kafka bootstrap servers: localhost:9092
- Kafka topic: order-events

## Notes

- This service uses in-memory storage. Data will be lost on restart.
- No database connection is required.
