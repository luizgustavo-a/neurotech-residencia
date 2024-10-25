FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/protalent-0.0.1-SNAPSHOT.jar protalent-app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "protalent-app.jar"]