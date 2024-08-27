# Employee Management API
## Description 
This is a Spring Boot application that manages employee data and enables the
functionality of exporting filtered employee data by year after
and department in csv or xlsx formats.

## Technologies used
1. Spring Boot - Framework for building the RESTful web service;
2. Spring Data JPA -  Provides data access and integration with relational databases;
3. Hibernate - ORM framework used for database interactions
4. JUnit 5 - Framework for writing and running tests;
5. Mockito - Framework for mocking dependencies;
6. MockMVC Testing Spring MVC controllers
7. Apache Commons CSV - Exporting data to CSV format;
8. Apache POI - Exporting data to Excel format;
9. Oracle DB - Database for data storage;
10. H2 database - In-memory database used for testing and development;
11. Lombok - Library to minimize boilerplate code.

## Structure

1. EmployeeController - Contains RESTful endpoints for managing employees.
2. EmployeeDAO - Entity class representing the employee data in the database.
3. EmployeeMapper - Maps between Employee and EmployeeDAO.
4. EmployeeRepository - Repository interface for CRUD operations.
5. EmployeeService - Service interface defining business logic.
6. EmployeeServiceImpl -  Implementation of the service interface.
7. FileProcessingException, GeneralDatabaseException - custom exceptions used in the application.
8. application.properties -  Main configuration file.

## Versions
Java 17 <br>
Spring Boot 3.3.3 <br>
Oracle DB 19c <br>

## Prerequisites
1. Java 17
2. Gradle
3. IDE (Intellij IDEA, Eclipse)
4. Oracle DB 19c

## Running the application
1. Clone the repository:
` git clone https://github.com/LRSSmeiksts/EmployeeManagement-API.git`
2. Configure Application. Update the _src/main/resources/application.properties_ with your database configuration
3. Run the application

## Testing
1. Run the tests in the IDE or using gradle: ./gradlew test
2. Use Postman to test the endpoints

## Endpoints 
POST /employees - create a new employee <br>
DELETE /employees/{id} - delete an employee by ID <br> 
GET /employees/export - Export preferred employee data to CSV or Excel filtering by year or department

## Postman 
POST: http://localhost:9090/employees <br>
Example JSON body:
```
{
"id": 2,
"name": "John",
"department": "IT",
"date": "2024-11-21"
}
```
DELETE: http://localhost:9090/employees/{id} <br>
Example: http://localhost:9090/employees/1

GET: http://localhost:9090/employees/export?department={department}&year={year}&format={format} <br>
Example: http://localhost:9090/employees/export?department=IT&year=2024&format=csv