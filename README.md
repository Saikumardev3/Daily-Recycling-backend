# Daily Recycling Backend (Spring Boot)

Backend API for Daily Recycling Error Correction System built with **Spring Boot**.

## Prerequisites

- Java 1.8 or higher
- Maven 3.6+
- PostgreSQL database (optional for testing)
- SMTP server credentials (optional for email notifications)

## Technology Stack

- **Spring Boot** 2.7.18 - Framework
- **PostgreSQL** - Database
- **Apache POI** - Excel processing
- **Spring Mail** - Email notifications

## Setup Instructions

1. **Navigate to the backend directory:**
   ```bash
   cd Daily-Recycling-backend
   ```

2. **Configure database connection (if needed):**
   Edit `src/main/resources/application.properties` and update:
   - Database URL, username, and password
   - Database schema name (default: `pcb`)

3. **Configure email settings (optional):**
   Edit `src/main/resources/application.properties` and update:
   - SMTP host, port
   - Email username and password

4. **Build the project:**
   ```bash
   mvn clean install
   ```

5. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

   Or run the JAR file:
   ```bash
   java -jar target/daily-recycling-backend-1.0.0.jar
   ```

6. **Verify the API is running:**
   ```bash
   curl http://localhost:8080/api/recycling/health
   ```

## API Endpoints

All endpoints are under `/api/recycling/`:

### Health Check
- `GET /api/recycling/health` - Check API health status

### Record Processing
- `POST /api/recycling/parse` - Parse fixed-width record
  - Body: `{ "record": "fixed-width-string" }`
  
- `POST /api/recycling/correct` - Correct record errors
  - Body: `{ "record": "fixed-width-string", "errorType": "START_DATE_END_DATE" }`

- `POST /api/recycling/batch` - Process batch of records

### Error Types
- `GET /api/recycling/errorTypes` - Get list of available error types

## Project Structure

```
src/main/
├── java/com/dailyrecycling/
│   ├── config/          # Configuration classes (CORS, etc.)
│   ├── controller/      # REST controllers
│   │   └── RecyclingController.java
│   ├── model/           # Data models and DTOs
│   └── service/         # Business logic services
│       ├── DatabaseService.java
│       ├── ErrorCorrectionService.java
│       ├── FixedWidthParser.java
│       └── EmailService.java
├── resources/
│   └── application.properties  # Spring Boot configuration
└── DailyRecyclingApplication.java  # Main application class
```

## Configuration

All configuration is in `src/main/resources/application.properties`. Key settings:
- Server port: `8080`
- CORS allowed origins: `http://localhost:3000`
- File upload max size: `10MB`

## Development

### Running in Development Mode
```bash
mvn spring-boot:run
```

The application will be available at `http://localhost:8080`

### Building for Production
```bash
mvn clean package
```

This creates a JAR file in the `target` directory that can be run standalone.

## Notes

- Spring Boot uses embedded Tomcat server
- No need for external application server
- All configuration is in `application.properties`
- Services are automatically detected via `@Service` annotation
