# Use the OpenJDK 17 base image
FROM maven:3.9.9-eclipse-temurin-21-alpine AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and download dependencies
COPY pom.xml .
COPY src ./src
COPY checkstyle.xml .
RUN mvn clean package -DskipTests

# User a smaller base image to run the application
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy the jar file from the previous stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port the backend uses
EXPOSE 8081

# Run the Spring Boot application
CMD ["java", "-jar", "app.jar"]
