# Inventory Service

DDD-based microservice for inventory management with Kafka integration.

## Features

- Domain-Driven Design architecture
- Kafka consumer for OrderCreated events
- Kafka producer for OrderApproved events
- Automatic stock reservation and validation
- In-memory data storage with pre-populated sample data
- RESTful API with OpenAPI documentation

## Architecture

The project follows DDD principles with clear layer separation:

- **Domain Layer**: Core business logic (Inventory, InventoryStatus, OrderApproved event)
- **Application Layer**: Use cases and DTOs (InventoryService, CreateInventoryRequest, InventoryResponse)
- **Infrastructure Layer**: Technical implementations (Kafka consumer/producer, In-memory repository)
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

The application will start on `http://localhost:8081`

## API Documentation

Swagger UI: `http://localhost:8081/swagger-ui.html`

## API Endpoints

### Get All Inventory
```bash
GET /api/inventory
```

### Get Inventory by Product ID
```bash
GET /api/inventory/{productId}
```

### Create/Update Inventory
```bash
POST /api/inventory
Content-Type: application/json

{
  "productId": "PROD-006",
  "productName": "Webcam",
  "availableQuantity": 20,
  "reservedQuantity": 0
}
```

## Kafka Integration

### Consumer

The service listens to the `order-events` topic for `OrderCreated` events from order-service.

When an OrderCreated event is received:
1. Checks inventory availability for all items
2. Reserves stock if all items are available
3. Publishes OrderApproved event with approval status

### Producer

Publishes `OrderApproved` events to the `order-approval-events` topic.

Event structure:
```json
{
  "orderId": "uuid",
  "approved": true,
  "reason": "Stock available and reserved successfully",
  "processedAt": "2025-11-26T16:50:00",
  "eventType": "ORDER_APPROVED"
}
```

If stock is insufficient:
```json
{
  "orderId": "uuid",
  "approved": false,
  "reason": "Insufficient stock for one or more items",
  "processedAt": "2025-11-26T16:50:00",
  "eventType": "ORDER_APPROVED"
}
```

## Pre-populated Inventory

The service starts with the following sample inventory:

| Product ID | Product Name | Available Quantity |
|------------|--------------|-------------------|
| PROD-001   | Laptop       | 10                |
| PROD-002   | Mouse        | 50                |
| PROD-003   | Keyboard     | 30                |
| PROD-004   | Monitor      | 5                 |
| PROD-005   | Headphones   | 0 (out of stock)  |

## Configuration

Key configuration in `application.yml`:
- Server port: 8081
- Kafka bootstrap servers: localhost:9092
- Consumer group: inventory-service-group
- Consumer topic: order-events
- Producer topic: order-approval-events

## Testing the Integration

1. Start Kafka locally
2. Start order-service (port 8080)
3. Start inventory-service (port 8081)
4. Create an order via order-service:
   ```bash
   curl -X POST http://localhost:8080/api/orders \
     -H "Content-Type: application/json" \
     -d '{
       "customerId": "CUST-001",
       "items": [
         {
           "productId": "PROD-001",
           "quantity": 2,
           "price": 999.99
         }
       ]
     }'
   ```
5. Check inventory-service logs to see:
   - OrderCreated event received
   - Stock reservation
   - OrderApproved event published
6. Verify inventory was updated:
   ```bash
   curl http://localhost:8081/api/inventory/PROD-001
   ```

## Notes

- This service uses in-memory storage. Data will be lost on restart.
- No database connection is required.
- The service automatically handles both successful and failed order processing scenarios.
