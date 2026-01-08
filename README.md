# Spring Boot Backend Template

A comprehensive, production-ready Spring Boot template for building scalable RESTful APIs with modern best practices, security, and clean architecture.

## 🎯 Features

- **Spring Boot 3.x** - Latest version with enhanced performance and features
- **RESTful API Design** - Clean and intuitive REST endpoints
- **Spring Data JPA** - ORM with easy database operations
- **Spring Security** - JWT authentication and authorization
- **Swagger/OpenAPI Documentation** - Auto-generated API documentation
- **Input Validation** - Bean Validation with custom validators
- **Global Exception Handling** - Centralized error management
- **Logging** - Comprehensive logging with SLF4J and Logback
- **Database Migrations** - Flyway for version control
- **Unit & Integration Testing** - JUnit 5, Mockito, and TestContainers
- **Docker Support** - Multi-stage Docker builds for optimized images
- **CI/CD Ready** - GitHub Actions configuration included
- **Environment Configuration** - Multi-environment support (dev, test, prod)
- **API Versioning** - Support for multiple API versions
- **Pagination & Filtering** - Built-in support for data pagination
- **CORS Configuration** - Flexible cross-origin resource sharing

## 📋 Prerequisites

Before you begin, ensure you have the following installed:

- **Java 17 or higher** - [Download JDK](https://www.oracle.com/java/technologies/downloads/)
- **Maven 3.8.1 or higher** - [Download Maven](https://maven.apache.org/download.cgi)
- **PostgreSQL 13 or higher** (or MySQL 8.0+) - [Download Database](https://www.postgresql.org/download/)
- **Docker & Docker Compose** (Optional) - [Download Docker](https://www.docker.com/products/docker-desktop)
- **Git** - [Download Git](https://git-scm.com/download)

## 🚀 Quick Start

### 1. Clone the Repository

```bash
git clone https://github.com/Shavez90/Spring-Boot-Backend-Template.git
cd Spring-Boot-Backend-Template
```

### 2. Configure Application Properties

Edit `src/main/resources/application.yml` with your database credentials:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/your_database
    username: your_username
    password: your_password
    driver-class-name: org.postgresql.Driver
  
  jpa:
    hibernate:
      ddl-auto: validate
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
```

### 3. Build the Project

```bash
mvn clean install
```

### 4. Run the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## 🐳 Docker Setup

### Using Docker Compose (Recommended)

The template includes a `docker-compose.yml` file that sets up both the application and database:

```bash
# Build and start all services
docker-compose up --build

# Stop services
docker-compose down

# View logs
docker-compose logs -f app
```

### Manual Docker Build

```bash
# Build Docker image
docker build -t spring-boot-api:latest .

# Run container with database
docker run -d \
  --name spring-boot-api \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/mydb \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=password \
  -p 8080:8080 \
  spring-boot-api:latest
```

## 📚 API Documentation

### Accessing Swagger UI

Once the application is running, access the interactive API documentation:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs
- **OpenAPI YAML**: http://localhost:8080/v3/api-docs.yaml

### API Base URL

```
http://localhost:8080/api/v1
```

## 🔐 Authentication Flow

### JWT Authentication Overview

This template uses JWT (JSON Web Tokens) for stateless authentication:

#### 1. User Registration

**Endpoint**: `POST /api/v1/auth/register`

```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "SecurePassword123!",
    "firstName": "John",
    "lastName": "Doe"
  }'
```

**Response**:
```json
{
  "id": 1,
  "email": "user@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "message": "User registered successfully"
}
```

#### 2. User Login

**Endpoint**: `POST /api/v1/auth/login`

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "SecurePassword123!"
  }'
```

**Response**:
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "expiresIn": 3600
}
```

#### 3. Using the Token

Add the JWT token to all subsequent requests:

```bash
curl -X GET http://localhost:8080/api/v1/users/profile \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

#### 4. Refresh Token

**Endpoint**: `POST /api/v1/auth/refresh`

```bash
curl -X POST http://localhost:8080/api/v1/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }'
```

#### 5. Logout

**Endpoint**: `POST /api/v1/auth/logout`

```bash
curl -X POST http://localhost:8080/api/v1/auth/logout \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

## 📊 CRUD Examples

### Create Entity Example

This example demonstrates creating a simple "Product" entity with CRUD operations.

#### 1. Create the JPA Entity

**File**: `src/main/java/com/template/entity/Product.java`

```java
@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 255)
    @NotBlank(message = "Product name is required")
    private String name;
    
    @Column(length = 1000)
    private String description;
    
    @Column(nullable = false)
    @DecimalMin(value = "0.0", message = "Price must be greater than 0")
    private BigDecimal price;
    
    @Column(nullable = false)
    @Min(value = 0, message = "Stock cannot be negative")
    private Integer stock;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
```

#### 2. Create the DTO (Data Transfer Object)

**File**: `src/main/java/com/template/dto/ProductDTO.java`

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    private Long id;
    
    @NotBlank(message = "Product name is required")
    private String name;
    
    private String description;
    
    @DecimalMin(value = "0.0", message = "Price must be greater than 0")
    private BigDecimal price;
    
    @Min(value = 0, message = "Stock cannot be negative")
    private Integer stock;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

#### 3. Create the Repository

**File**: `src/main/java/com/template/repository/ProductRepository.java`

```java
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    Optional<Product> findByName(String name);
    
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
    @Query("SELECT p FROM Product p WHERE p.stock > 0 ORDER BY p.createdAt DESC")
    Page<Product> findAvailableProducts(Pageable pageable);
}
```

#### 4. Create the Service

**File**: `src/main/java/com/template/service/ProductService.java`

```java
@Service
@Slf4j
@Transactional
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    // CREATE
    public ProductDTO createProduct(ProductDTO productDTO) {
        log.info("Creating new product: {}", productDTO.getName());
        
        Product product = modelMapper.map(productDTO, Product.class);
        Product savedProduct = productRepository.save(product);
        
        log.info("Product created with ID: {}", savedProduct.getId());
        return modelMapper.map(savedProduct, ProductDTO.class);
    }
    
    // READ - Get all products with pagination
    @Transactional(readOnly = true)
    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        log.info("Fetching all products with pagination");
        return productRepository.findAll(pageable)
                .map(product -> modelMapper.map(product, ProductDTO.class));
    }
    
    // READ - Get product by ID
    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long id) {
        log.info("Fetching product with ID: {}", id);
        
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
        
        return modelMapper.map(product, ProductDTO.class);
    }
    
    // UPDATE
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        log.info("Updating product with ID: {}", id);
        
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
        
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());
        
        Product updatedProduct = productRepository.save(product);
        log.info("Product updated successfully");
        
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }
    
    // DELETE
    public void deleteProduct(Long id) {
        log.info("Deleting product with ID: {}", id);
        
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with ID: " + id);
        }
        
        productRepository.deleteById(id);
        log.info("Product deleted successfully");
    }
}
```

#### 5. Create the REST Controller

**File**: `src/main/java/com/template/controller/ProductController.java`

```java
@RestController
@RequestMapping("/api/v1/products")
@Slf4j
@Validated
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<ProductDTO>> createProduct(
            @Valid @RequestBody ProductDTO productDTO) {
        log.info("Received request to create product");
        
        ProductDTO createdProduct = productService.createProduct(productDTO);
        
        return ResponseEntity.ok(ApiResponse.<ProductDTO>builder()
                .status("success")
                .message("Product created successfully")
                .data(createdProduct)
                .timestamp(LocalDateTime.now())
                .build());
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<Page<ProductDTO>>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction) {
        
        log.info("Received request to fetch all products - page: {}, size: {}", page, size);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<ProductDTO> products = productService.getAllProducts(pageable);
        
        return ResponseEntity.ok(ApiResponse.<Page<ProductDTO>>builder()
                .status("success")
                .message("Products retrieved successfully")
                .data(products)
                .timestamp(LocalDateTime.now())
                .build());
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<ProductDTO>> getProductById(@PathVariable Long id) {
        log.info("Received request to fetch product with ID: {}", id);
        
        ProductDTO product = productService.getProductById(id);
        
        return ResponseEntity.ok(ApiResponse.<ProductDTO>builder()
                .status("success")
                .message("Product retrieved successfully")
                .data(product)
                .timestamp(LocalDateTime.now())
                .build());
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductDTO>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductDTO productDTO) {
        
        log.info("Received request to update product with ID: {}", id);
        
        ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
        
        return ResponseEntity.ok(ApiResponse.<ProductDTO>builder()
                .status("success")
                .message("Product updated successfully")
                .data(updatedProduct)
                .timestamp(LocalDateTime.now())
                .build());
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        log.info("Received request to delete product with ID: {}", id);
        productService.deleteProduct(id);
    }
}
```

#### 6. API Endpoints Summary

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|----------------|
| POST | `/api/v1/products` | Create a new product | ADMIN |
| GET | `/api/v1/products` | Get all products with pagination | USER, ADMIN |
| GET | `/api/v1/products/{id}` | Get product by ID | USER, ADMIN |
| PUT | `/api/v1/products/{id}` | Update product | ADMIN |
| DELETE | `/api/v1/products/{id}` | Delete product | ADMIN |

## 🧪 Testing

### Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=ProductServiceTest

# Run with coverage
mvn clean test jacoco:report
```

### Example Unit Test

**File**: `src/test/java/com/template/service/ProductServiceTest.java`

```java
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    
    @Mock
    private ProductRepository productRepository;
    
    @Mock
    private ModelMapper modelMapper;
    
    @InjectMocks
    private ProductService productService;
    
    private Product testProduct;
    private ProductDTO testProductDTO;
    
    @BeforeEach
    void setUp() {
        testProduct = Product.builder()
                .id(1L)
                .name("Test Product")
                .description("Test Description")
                .price(BigDecimal.valueOf(99.99))
                .stock(10)
                .build();
        
        testProductDTO = ProductDTO.builder()
                .id(1L)
                .name("Test Product")
                .description("Test Description")
                .price(BigDecimal.valueOf(99.99))
                .stock(10)
                .build();
    }
    
    @Test
    void testGetProductById_Success() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(modelMapper.map(testProduct, ProductDTO.class)).thenReturn(testProductDTO);
        
        // Act
        ProductDTO result = productService.getProductById(1L);
        
        // Assert
        assertNotNull(result);
        assertEquals("Test Product", result.getName());
        verify(productRepository, times(1)).findById(1L);
    }
    
    @Test
    void testGetProductById_NotFound() {
        // Arrange
        when(productRepository.findById(99L)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            productService.getProductById(99L);
        });
    }
}
```

## 🏗️ Project Structure

```
Spring-Boot-Backend-Template/
├── src/
│   ├── main/
│   │   ├── java/com/template/
│   │   │   ├── config/              # Configuration classes
│   │   │   ├── controller/          # REST Controllers
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   ├── entity/              # JPA Entities
│   │   │   ├── exception/           # Custom Exceptions
│   │   │   ├── repository/          # JPA Repositories
│   │   │   ├── security/            # Security configuration
│   │   │   ├── service/             # Business Logic
│   │   │   └── Application.java
│   │   └── resources/
│   │       ├── application.properties  # Main configuration
│   │       ├── application-dev.yml     # Development config
│   │       ├── application-prod.yml    # Production config
│   │       └── db/migration/           # Flyway migrations
│   └── test/
│       └── java/com/template/       # Unit & Integration Tests
├── docker/
│   └── Dockerfile                   # Multi-stage Docker build
├── docker-compose.yml               # Docker Compose setup
├── pom.xml                          # Maven dependencies
├── .github/workflows/               # CI/CD workflows
└── README.md                        # This file
```

## ⚙️ Configuration

### Environment Variables

Create a `.env` file in the project root:

```env
# Database Configuration
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/mydb
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=password

# JWT Configuration
JWT_SECRET=your-super-secret-key-min-32-characters-long
JWT_EXPIRATION=3600000
JWT_REFRESH_EXPIRATION=604800000

# Application
SERVER_PORT=8080
SERVER_SERVLET_CONTEXT_PATH=/api
SPRING_PROFILES_ACTIVE=dev
```

### Application Properties

**File**: `src/main/resources/application.yml`

```yaml
spring:
  application:
    name: spring-boot-api
  
  datasource:
    url: ${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5432/mydb}
    username: ${SPRING_DATASOURCE_USERNAME:postgres}
    password: ${SPRING_DATASOURCE_PASSWORD:password}
    driver-class-name: org.postgresql.Driver
    
  jpa:
    hibernate:
      ddl-auto: validate
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
    show-sql: false
  
  flyway:
    locations: classpath:db/migration
    baseline-on-migrate: true

server:
  port: ${SERVER_PORT:8080}
  servlet:
    context-path: /api

jwt:
  secret: ${JWT_SECRET:default-secret-key-min-32-chars-required}
  expiration: ${JWT_EXPIRATION:3600000}
  refresh-expiration: ${JWT_REFRESH_EXPIRATION:604800000}

logging:
  level:
    root: INFO
    com.template: DEBUG
```

## 📦 Dependencies

Key dependencies included:

```xml
<!-- Spring Boot -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- Database -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
</dependency>

<!-- Validation -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>

<!-- JWT -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
</dependency>

<!-- API Documentation -->
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
</dependency>

<!-- Lombok -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <scope>provided</scope>
</dependency>

<!-- ModelMapper -->
<dependency>
    <groupId>org.modelmapper</groupId>
    <artifactId>modelmapper</artifactId>
</dependency>

<!-- Testing -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

## 🚀 Using This Template for Your Project

This template is designed for quick reuse. Follow these steps to customize it:

### Step 1: Clone the Repository
```bash
git clone https://github.com/Shavez90/Spring-Boot-Backend-Template.git your-project-name
cd your-project-name
```

### Step 2: Rename Package Structure
Do a global find and replace in your IDE:
- Find: `com.template`
- Replace: `com.yourcompany.yourproject`

### Step 3: Rename Main Application Class
- Rename `Application.java` to `YourProjectApplication.java`
- Update the class name inside the file

### Step 4: Update pom.xml
Update these values:
```xml
<groupId>com.yourcompany</groupId>
<artifactId>your-project-name</artifactId>
<name>Your Project Name</name>
<description>Your project description</description>
```

### Step 5: Update Application Properties
Update `src/main/resources/application.properties` or `application.yml`:
- Database connection settings
- JWT secret key
- Application name

### Step 6: Initialize New Git Repository (Optional)
```bash
rm -rf .git
git init
git add .
git commit -m "Initial commit from template"
```

### Step 7: Start Coding! 🎉
```bash
mvn clean install
mvn spring-boot:run
```

## 🔄 CI/CD Pipeline

The template includes GitHub Actions workflow for automated testing and deployment.

**File**: `.github/workflows/build.yml`

```yaml
name: Build & Test

on: [push, pull_request]

jobs:
  build:
    runs-on: ubuntu-latest
    
    services:
      postgres:
        image: postgres:15
        env:
          POSTGRES_PASSWORD: postgres
        options: >-
          --health-cmd pg_isready
          --health-interval 10s
          --health-timeout 5s
          --health-retries 5

    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
      
      - name: Build with Maven
        run: mvn clean install
      
      - name: Run tests
        run: mvn test
```

## 🚨 Error Handling

The template includes global exception handling with custom exceptions:

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFound(
            ResourceNotFoundException ex) {
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.builder()
                        .status("error")
                        .message(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build());
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.builder()
                        .status("error")
                        .message("Validation failed")
                        .errors(errors)
                        .timestamp(LocalDateTime.now())
                        .build());
    }
}
```

## 📝 Logging

The template uses SLF4J with Logback for comprehensive logging:

```yaml
logging:
  level:
    root: INFO
    com.template: DEBUG
    org.springframework.security: DEBUG
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} - %logger{36} - %msg%n"
  file:
    name: logs/application.log
    max-size: 10MB
    max-history: 10
```

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙋 Support

For issues, questions, or suggestions, please:

1. Check existing [Issues](https://github.com/Shavez90/Spring-Boot-Backend-Template/issues)
2. Create a new issue with detailed description
3. Join our discussion forum (if available)

## 📚 Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security Guide](https://spring.io/guides/topicals/spring-security-architecture/)
- [Spring Data JPA Documentation](https://spring.io/projects/spring-data-jpa)
- [JWT Best Practices](https://tools.ietf.org/html/rfc7519)
- [RESTful API Design Guidelines](https://restfulapi.net/)
- [Docker Documentation](https://docs.docker.com/)

## 📧 Contact

**Author**: Shavez90  
**Email**: [your-email@example.com](mailto:your-email@example.com)  
**GitHub**: [@Shavez90](https://github.com/Shavez90)

---

**Last Updated**: January 8, 2026

**Happy Coding! 🚀**
