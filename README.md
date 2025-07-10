# Order Simulator Service

### Key Components
- **Controllers**: REST endpoints exposing order management functionality
- **Services**: Business logic implementation
- **Ports & Adapters**: Interface-based boundary between domain and external resources
- **Repositories**: Data access layer for order persistence

## Functionality

### Order Management
- **Create Order**: Submit new buy/sell orders with symbol, price, and quantity
- **Get Orders**: Retrieve all orders or specific orders by ID
- **Cancel Order**: Cancel an existing order
- **Simulate Order Execution**: Randomly process pending orders to simulate market activity

### API Endpoints
- `POST /simulator/order` - Create a new order
- `GET /simulator/order` - List all orders
- `GET /simulator/order/{id}` - Get specific order details
- `POST /simulator/order/{id}/cancel` - Cancel an order
- `POST /simulator/order/simulate-execution` - Trigger order execution simulation

## Technical Stack
- Java 21
- Spring Boot
- Spring Data JPA
- Spring Security
- Swagger/OpenAPI for API documentation
- JUnit 5 & Mockito for testing
- Maven for build management
- log4j2 for logging

## Running the Service
1. Clone the repository
2. Run `mvn clean install`
3. Start the service with `mvn spring-boot:run`
4. Access the API documentation at `http://localhost:8080/swagger-ui.html`

## Running the unit tests
Run `mvn test` to execute all unit tests. The service uses JUnit 5 and Mockito for testing.

## Authentication
The service uses Basic Authentication. In memory users are configured with the following credentials:
1. Role ADMIN 
   - Username: `admin`
   - Password: `123456`
2. Role EMPLOYEE 
   - Username: `employee`
   - Password: `123456`

## Curl Api Examples
### Create Order
```bash
curl --location 'http://localhost:8080/simulator/order' \
--header 'accept: */*' \
--header 'Content-Type: application/json' \
--header 'Authorization: Basic YWRtaW46MTIzNDU2' \
--header 'Cookie: JSESSIONID=B28A76E18E75F398BDB291CABA4E398B' \
--data '{
  "symbol": "AAPL",
  "price": 150.5,
  "quantity": 100,
  "side": "BUY",
  "status": "PENDING"
}'
```

### Get All Orders
```bash
curl --location 'http://localhost:8080/simulator/order' \
--header 'accept: */*' \
--header 'Authorization: Basic YWRtaW46MTIzNDU2' \
--header 'Cookie: JSESSIONID=9EFFA42BC466BF36C43199EAEA5D4EFC'
```
### Get Order by ID
```bash
curl --location 'http://localhost:8080/simulator/order/1' \
--header 'accept: */*' \
--header 'Authorization: Basic YWRtaW46MTIzNDU2' \
--header 'Cookie: JSESSIONID=9EFFA42BC466BF36C43199EAEA5D4EFC'
```
### Cancel Order
```bash
curl --location --request POST 'http://localhost:8080/simulator/order/1/cancel' \
--header 'accept: */*' \
--header 'Authorization: Basic YWRtaW46MTIzNDU2' \
--header 'Cookie: JSESSIONID=9EFFA42BC466BF36C43199EAEA5D4EFC'
```
### Simulate Order Execution
```bash
curl --location --request POST 'http://localhost:8080/simulator/order/simulate-execution' \
--header 'accept: */*' \
--header 'Authorization: Basic YWRtaW46MTIzNDU2' \
--header 'Cookie: JSESSIONID=9EFFA42BC466BF36C43199EAEA5D4EFC'
```