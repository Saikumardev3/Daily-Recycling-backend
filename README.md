# Daily Recycling Backend (Apache Struts 2)

Backend API for Daily Recycling Error Correction System built with **Apache Struts 2** and Spring Framework.

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL database
- Application Server (Tomcat 9+, or use embedded Tomcat via Maven plugin)
- SMTP server credentials (for email notifications)

## Technology Stack

- **Apache Struts 2** 6.3.0.2 - MVC Framework
- **Spring Framework** 5.3.31 - Dependency Injection & JDBC
- **PostgreSQL** - Database
- **Apache POI** - Excel processing
- **Jackson** - JSON processing

## Setup Instructions

1. **Clone and navigate to the backend directory:**
   ```bash
   cd Daily-Recycling-backend
   ```

2. **Configure database connection:**
   Edit `src/main/resources/applicationContext.xml` and update:
   - Database URL, username, and password
   - Database schema name (default: `pcb`)

3. **Configure email settings:**
   Edit `src/main/resources/applicationContext.xml` and update:
   - SMTP host, port
   - Email username and password

4. **Build the project:**
   ```bash
   mvn clean install
   ```

5. **Run the application:**

   **Option A: Using Tomcat Maven Plugin (Recommended for development)**
   ```bash
   mvn tomcat7:run
   ```

   **Option B: Deploy WAR to Tomcat**
   ```bash
   # Copy the WAR file to Tomcat webapps directory
   cp target/daily-recycling-backend.war $TOMCAT_HOME/webapps/
   # Start Tomcat
   $TOMCAT_HOME/bin/startup.sh
   ```

   **Option C: Using IDE (IntelliJ IDEA / Eclipse)**
   - Configure Tomcat server
   - Deploy the WAR file
   - Run the server

6. **Verify the API is running:**
   ```bash
   curl http://localhost:8080/api/recycling/health.action
   ```

## API Endpoints

All endpoints are under `/api/recycling/` namespace:

### Health Check
- `GET /api/recycling/health.action` - Check API health status

### Record Processing
- `POST /api/recycling/parse.action` - Parse fixed-width record
  - Body: `{ "record": "fixed-width-string" }`
  
- `POST /api/recycling/correct.action` - Correct record errors
  - Body: `{ "record": "fixed-width-string", "errorType": "START_DATE_END_DATE" }`

- `POST /api/recycling/batch.action` - Process batch of records

### Error Types
- `GET /api/recycling/errorTypes.action` - Get list of available error types

## Project Structure

```
src/main/
├── java/com/dailyrecycling/
│   ├── action/          # Struts Action classes
│   │   └── RecyclingAction.java
│   ├── filter/          # Servlet filters
│   │   └── CORSFilter.java
│   ├── model/           # Data models and DTOs
│   └── service/         # Business logic services
│       ├── DatabaseService.java
│       ├── ErrorCorrectionService.java
│       ├── FixedWidthParser.java
│       └── EmailService.java
├── resources/
│   ├── struts.xml       # Struts configuration
│   └── applicationContext.xml  # Spring configuration
└── webapp/
    └── WEB-INF/
        └── web.xml      # Web application configuration
```

## Configuration Files

### struts.xml
Struts 2 action mapping and configuration.

### applicationContext.xml
Spring bean definitions for services, database, and email.

### web.xml
Servlet configuration, filters, and Spring context loader.

## Database Schema

The application connects to the following database tables:
- `pcb.pcbqtiny` - Policy transactions
- `pcb.pcbqtaso` - Policy actors
- `pcb.pcbqtadr` - Addresses
- `pcb.pcbqtaco` - BCU numbers

## Development

### Running in Development Mode
```bash
mvn tomcat7:run
```

The application will be available at `http://localhost:8080`

### Debugging
- Set breakpoints in Action classes
- Check logs in console
- Struts devMode is enabled by default (see struts.xml)

## CORS Configuration

CORS is configured to allow requests from `http://localhost:3000` (React frontend).
Update `CORSFilter.java` or `web.xml` for production CORS settings.

## Notes

- Struts 2 uses convention-based action mapping
- Spring integration allows dependency injection into Actions
- JSON responses are handled by Struts JSON plugin
- All services are managed by Spring context
