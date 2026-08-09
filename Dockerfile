# syntax=docker/dockerfile:1

# ================================
# Stage 1: Build the application
# ================================
FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /app

# Copy Maven wrapper and project configuration
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Download dependencies
RUN ./mvnw dependency:go-offline -DskipTests

# Copy source code
COPY src ./src

# Build Spring Boot application
RUN ./mvnw clean package -DskipTests


# ================================
# Stage 2: Run the application
# ================================
FROM eclipse-temurin:17-jre-jammy AS runtime

WORKDIR /app

# Copy the generated JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Spring Boot port
EXPOSE 8080

# Start application
ENTRYPOINT ["java", "-jar", "app.jar"]