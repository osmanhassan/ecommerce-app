# ===============================
# Stage 1: Build the Spring Boot App
# ===============================
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml and download dependencies first (better caching)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code and build application
COPY src ./src
RUN mvn clean package -DskipTests

# ===============================
# Stage 2: Run the Spring Boot App
# ===============================
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copy only the final JAR from the previous stage
COPY --from=build /app/target/ecommerce-app-0.0.1-SNAPSHOT.jar ecommerce-app.jar

# Expose app port
EXPOSE 8080

# Default command (Profile will be passed via ENV from docker-compose)
ENTRYPOINT ["java", "-jar", "ecommerce-app.jar"]
