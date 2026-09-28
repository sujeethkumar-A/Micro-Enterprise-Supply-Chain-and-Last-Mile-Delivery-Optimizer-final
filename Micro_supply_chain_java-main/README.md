# Micro-Enterprise Supply Chain & Last-Mile Delivery Optimizer

**Problem Statement 12:** Micro-Enterprise Supply Chain & Last-Mile Delivery Optimizer  
**Project Level:** Mid-Level College LEAP Project  
**Technology Stack:** Java 21, Spring Boot 3.3.4, Spring MVC, Spring Data JPA, Hibernate, MySQL, Thymeleaf, HTML5, CSS3, JavaScript, Maven

---

## 1. Problem Statement & Background

Micro and small enterprises (such as local distributors, retail suppliers, and small manufacturing workshops) struggle with fragmented, manual processes for handling:
- Supplier vendor communication and stock intake
- Multi-category inventory management and tracking safety stock levels
- Customer order placement and stock validation
- Last-mile parcel delivery planning and route dispatch

Manual route planning leads to excessive fuel costs, delayed customer delivery times, and inventory stock-outs during high-demand periods.

### Proposed Solution
A centralized, practical, web-based logistics management system engineered specifically for micro-enterprises. It couples **automated inventory threshold alerting** with an intuitive **Nearest-Neighbour Last-Mile Route Optimization Heuristic** to minimize distribution travel distance, predict delivery costs, and prevent stock-outs.

---

## 2. Core Features

1. **Operations Dashboard:**
   - Real-time database metrics: Total Suppliers, Products, Customers, Orders, Deliveries, and Stock Deficit Alerts.
   - Live critical low-stock alert table (`stockQuantity <= minimumStock`).
   - Quick one-click restock actions.

2. **Supplier Management:**
   - Full CRUD operations for sourcing vendors, contact persons, phone numbers, and addresses.
   - Search by name and responsive pagination.

3. **Products & Inventory Control:**
   - Catalog management with categories (Packaging, Electronics, Stationery, Raw Materials).
   - Unit pricing, current stock, and safety stock threshold.
   - Low-stock badge detection with instant filter (`lowStockOnly=true`).
   - Quick +/- stock adjustment buttons.

4. **Customer Geolocation Management:**
   - Customer registry with contact data and precise GPS coordinates (`latitude`, `longitude`).
   - Geolocation-enabled address tracking for last-mile routing.

5. **Order Processing with Stock Validation:**
   - Multi-product line item dynamic order placement.
   - Client-side live stock checks and instant subtotal/grand total calculations.
   - **Strict `@Transactional` Stock Validation:** If requested quantity > available inventory, throws `InsufficientStockException` and aborts order creation.
   - Automatic deduction of product inventory upon order confirmation.
   - Order cancellation restoring inventory back to the warehouse.
   - Automatic dispatch scheduling into the delivery queue.

6. **Last-Mile Delivery Route Optimizer (Main Feature):**
   - **Nearest-Neighbour Greedy Heuristic:** Dispatches vehicles from the central enterprise depot to the nearest pending customer, iteratively finding the next closest unvisited customer.
   - **Haversine Formula Distance:** Accurate great-circle distance computation between GPS coordinates.
   - **Configurable Delivery Cost Formula:** `Base Cost (₹30) + [Total Distance (km) × ₹8/km]`.
   - **Estimated Travel Time:** Calculated using average city delivery speed (30 km/h) in minutes.
   - Visual stop-by-stop route timeline and leg-by-leg metrics table.

7. **REST APIs:**
   - RESTful endpoints (`/api/products`, `/api/customers`, `/api/orders`, `/api/deliveries`, `/api/routes/optimize`) returning clean DTOs.

---

## 3. Technology Stack

| Layer | Technology |
|---|---|
| **Language** | Java 21 LTS |
| **Framework** | Spring Boot 3.3.4 (Spring MVC, Spring Data JPA, Spring Validation) |
| **ORM / Persistence** | Hibernate 6.x via Spring Data JPA |
| **Database** | MySQL 8.0 (`micro_supply_chain` database) |
| **Template Engine** | Thymeleaf 3 |
| **Frontend** | HTML5, Vanilla CSS3 (Custom Design System), JavaScript ES6+ |
| **Build Tool** | Apache Maven 3.9+ |
| **Testing** | JUnit 5, Mockito, Spring Boot Starter Test |

---

## 4. Layered Architecture

The application strictly adheres to a clean, viva-friendly Spring Boot layered architecture:

```
[ Web Browser (HTML / CSS / JS / Thymeleaf) ]       [ External REST Client / JSON ]
                     │                                              │
             Thymeleaf Controller                           REST Controller
             (@Controller)                                 (@RestController)
                     │                                              │
                     └──────────────────────┬───────────────────────┘
                                            ▼
                                   Data Transfer Object (DTO)
                                            │
                                            ▼
                                      Service Layer
                                 (@Service, @Transactional)
                                 - Business Rules & Validation
                                 - Nearest-Neighbour Routing
                                            │
                                            ▼
                                    Data Access Object (DAO)
                                  (@Repository / DAO Interface)
                                            │
                                            ▼
                                   Spring Data JPA Repository
                                     (JpaRepository<T, ID>)
                                            │
                                            ▼
                                    JPA / Hibernate ORM
                                            │
                                            ▼
                                    MySQL Database
                                 (micro_supply_chain)
```

---

## 5. Entity Relationships (ER Model)

- **Supplier (1) ── (N) Product:** One supplier provides multiple catalog items.
- **Product (1) ── (N) OrderItem:** Each order line references a product.
- **Customer (1) ── (N) CustomerOrder:** One customer places multiple orders.
- **CustomerOrder (1) ── (N) OrderItem:** An order contains multiple product line items.
- **CustomerOrder (1) ── (1) Delivery:** An order generates an associated delivery tracking record.

---

## 6. Route Optimization & Mathematics

### A. Nearest-Neighbour Greedy Heuristic
1. **Start:** The vehicle begins at the Enterprise Central Depot (`lat0`, `lon0`).
2. **Step 1:** Calculate distance from current position to all pending unvisited customer locations.
3. **Step 2:** Select the customer with the minimum distance.
4. **Step 3:** Move vehicle to this customer location, mark as visited, and record leg distance, cumulative time, and cost.
5. **Step 4:** Repeat Steps 1–3 until all designated delivery stops are sequenced.

> *Note for Viva:* The nearest-neighbour algorithm is a greedy heuristic that provides an intuitive, computationally efficient route with $O(N^2)$ complexity. It does not guarantee the global mathematical optimum (which is NP-hard Travelling Salesperson Problem), but solves last-mile distribution effectively for micro-enterprises without heavy AI/ML libraries.

### B. Haversine Distance Formula
Calculates the great-circle distance between two points on a sphere of radius $R = 6371.0 \text{ km}$:

$$\Delta \text{lat} = \text{radians}(\text{lat}_2 - \text{lat}_1), \quad \Delta \text{lon} = \text{radians}(\text{lon}_2 - \text{lon}_1)$$
$$a = \sin^2\left(\frac{\Delta \text{lat}}{2}\right) + \cos(\text{lat}_1) \cdot \cos(\text{lat}_2) \cdot \sin^2\left(\frac{\Delta \text{lon}}{2}\right)$$
$$c = 2 \cdot \text{atan2}\left(\sqrt{a}, \sqrt{1 - a}\right)$$
$$\text{Distance} = R \cdot c \quad (\text{in kilometers})$$

### C. Delivery Cost & Transit Time Formula
- **Base Cost:** ₹30
- **Cost per Kilometer:** ₹8/km
$$\text{Total Cost} = \text{Base Cost} + (\text{Distance} \times \text{Cost per Km}) = 30 + (\text{Distance} \times 8)$$
- **Transit Time (@ 30 km/h average city transit speed):**
$$\text{Estimated Time (minutes)} = \left(\frac{\text{Distance}}{30}\right) \times 60$$

---

## 7. How to Configure & Run

### Prerequisites
- Java 21 JDK installed (`java -version`)
- Apache Maven installed (`mvn -v`)
- MySQL 8.0 running locally on port 3306

### Step 1: Ensure MySQL Database Exists
Open MySQL command line or MySQL Workbench:
```sql
CREATE DATABASE IF NOT EXISTS micro_supply_chain;
```

### Step 2: Configure Credentials
In `src/main/resources/application.properties`, verify local development connection:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/micro_supply_chain?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=sujeeth940
spring.jpa.hibernate.ddl-auto=update
```

### Step 3: Run Tests & Build
```bash
# Execute unit and service tests
mvn clean test

# Build executable JAR package
mvn clean package -DskipTests
```

### Step 4: Launch the Application
```bash
mvn spring-boot:run
```
The application will launch on:  
👉 **http://localhost:8080**

Automatic seed data (3 suppliers, 8 products with 2 low-stock, 5 customers, 5 orders, 3 pending deliveries) will be loaded automatically on first startup.

---

## 8. Web URLs & REST API Endpoints

### Thymeleaf Web Application Views
| URL | View Description |
|---|---|
| `GET /` or `GET /dashboard` | Main Operations Dashboard with real-time counters & alerts |
| `GET /suppliers` | Suppliers directory with search and pagination |
| `GET /suppliers/new` | Add new supplier form |
| `GET /products` | Products inventory with category and low-stock filter |
| `GET /products/new` | Add new catalog item with safety threshold |
| `GET /customers` | Customers listing with GPS geolocation coordinates |
| `GET /customers/new` | Register new customer with coordinates |
| `GET /orders` | Customer orders listing with status filtering |
| `GET /orders/new` | Dynamic multi-item order placement with stock check |
| `GET /orders/{id}` | Order details, customer address, and status update |
| `GET /deliveries` | Last-mile delivery parcels queue |
| `GET /deliveries/{id}` | Delivery parcel details, cost breakdown, and transit time |
| `GET /routes` | Interactive Last-Mile Nearest-Neighbour Route Optimizer |

### REST APIs
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/products` | Retrieve all products (JSON DTOs) |
| `GET` | `/api/products/{id}` | Get product by ID |
| `GET` | `/api/products/low-stock` | Get items below minimum stock threshold |
| `POST` | `/api/products` | Create product via JSON |
| `GET` | `/api/customers` | Retrieve all customer profiles |
| `POST` | `/api/customers` | Register customer with GPS coordinates |
| `GET` | `/api/orders` | List customer orders |
| `POST` | `/api/orders` | Place order with JSON body & transactional stock check |
| `GET` | `/api/deliveries` | List all delivery tracking records |
| `PUT` | `/api/deliveries/{id}/status` | Update delivery status (`{"status":"DELIVERED"}`) |
| `POST` | `/api/routes/optimize` | Compute nearest-neighbour itinerary for given deliveries |

---

## 9. Viva Questions & Architectural Reference

### Q1: What is Spring Boot?
**Answer:** Spring Boot is an opinionated, production-ready extension of the Spring Framework. It provides auto-configuration, embedded Tomcat servers, and pre-packaged starter dependencies (`spring-boot-starter-web`, `spring-boot-starter-data-jpa`) eliminating boilerplate XML and setup configuration.

### Q2: What is the role of the Controller vs Service vs DAO / Repository?
**Answer:**
- **Controller (`@Controller` / `@RestController`):** Handles incoming HTTP requests, binds parameters/DTOs, and delegates to services.
- **Service (`@Service`):** Houses business logic, such as stock validation, order discount rules, and nearest-neighbour path calculation.
- **DAO (`ProductDAO` / `OrderDAO`):** Abstracts data access patterns away from business rules.
- **Repository (`JpaRepository`):** Spring Data JPA interface that provides automatic CRUD queries (`save`, `findById`, `delete`) and JPQL queries.

### Q3: Why is `@Transactional` critical when placing an order?
**Answer:** In `OrderService.createOrder()`, multiple database updates take place:
1. Verifying available stock
2. Deducting quantity from the `product` table
3. Creating the `customer_order` record
4. Creating individual `order_item` records
If an error occurs or stock is insufficient for a second product, `@Transactional` automatically rolls back all previous changes, ensuring database consistency and preventing inventory corruption.

### Q4: How is low stock detected?
**Answer:** Each product defines `stockQuantity` and `minimumStock`. When `stockQuantity <= minimumStock`, the entity and queries flag the item as critical low stock, immediately surfacing it on the Dashboard and alerting the store manager to restock.

### Q5: How does the Route Optimizer work?
**Answer:** Using the customer's GPS latitude and longitude, the service applies the Haversine formula to compute great-circle distances in kilometers. Starting from the Enterprise Depot, the nearest-neighbour greedy algorithm iteratively selects the closest unvisited stop until all deliveries are sequenced. Total distance, transit time (@ 30 km/h), and cost (₹30 base + ₹8/km) are then computed and displayed on an interactive visual timeline.
