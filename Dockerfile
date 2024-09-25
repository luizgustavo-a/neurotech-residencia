FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/employee-management-0.0.1-SNAPSHOT.jar neurotech-employees-app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "neurotech-employees-app.jar"]