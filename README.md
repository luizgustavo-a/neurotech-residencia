# Neurotech Employee Management Application

This is a Spring Boot application designed for managing employee data. It allows CRUD operations on employee records, including filtering based on various attributes such as technical competences, certifications, and years of experience.

## Features

- Create, read, update, and delete employee records.
- Filter employees based on:
  - Technical competences
  - Certifications
  - Years of experience (minimum and maximum)
- Search for an employee by ID or email.
- Dockerized with PostgreSQL integration.

## Technologies Used

- **Java (Spring Boot)** for the backend.
- **PostgreSQL** as the database.
- **Docker** for containerization.
- **Flyway** for database migrations.
- **Hibernate** for ORM (Object Relational Mapping).

## Project Structure

### Services

- **app**: The main Spring Boot application that handles the employee management operations.
- **postgres**: A PostgreSQL container used for storing employee data.

### Environment Variables

The application uses the following environment variables:

- `POSTGRESQL_DB_HOST`: The hostname for the PostgreSQL database.
- `POSTGRESQL_DB_USER`: The PostgreSQL database user.
- `POSTGRESQL_DB_PASSWORD`: The PostgreSQL database password.

## Docker Setup

This project is Dockerized and uses `docker-compose` for setting up the application and the database.

### Running the Application

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/your-repo-name.git
   ```

2. Navigate into the project directory:
   ```bash
   cd your-repo-name
   ```

3. Build and run the application using Docker Compose:
   ```bash
   docker-compose up --build
   ```

4. The application will be available at:
   ```
   http://localhost:8080
   ```

## Endpoints

### Employee Endpoints

- **POST /employee**: Create a new employee.
- **GET /employee**: List all employees with optional filters.
- **GET /employee/{idOrEmail}**: Retrieve employee details by ID or email.
- **PUT /employee**: Update employee details.
- **DELETE /employee/{id}**: Delete an employee by ID.

### Filters (GET /employee)
- `technical competence`: List of technical competences.
- `certification`: List of certifications.
- `years of experience (min)`: Minimum years of experience.
- `years of experience (max)`: Maximum years of experience.

## Database

The application uses a PostgreSQL database with the following setup:

- **Database Name**: `neurotech_employees`
- **Tables**: Automatically created and managed by Hibernate and Flyway.

## Contributing

1. Fork the repository.
2. Create a new branch (`git checkout -b feature-branch`).
3. Commit your changes (`git commit -m 'Add new feature'`).
4. Push to the branch (`git push origin feature-branch`).
5. Open a pull request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
```
