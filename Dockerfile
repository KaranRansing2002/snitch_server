# Stage 1: Build the Spring Boot app
FROM maven:3.8.1-openjdk-11-slim AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project files
COPY ./spring_boot_backend_template /app

# Run Maven to build the application and skip tests to save time
RUN mvn clean package -DskipTests

# Stage 2: Use a smaller runtime image for running the app
FROM openjdk:11-jre-slim

# Set the working directory for the runtime container
WORKDIR /app

# Copy only the built JAR from the first stage
COPY --from=build /app/target/spring_boot_backend_template-0.0.1.jar /app/app.jar

# Expose the application's port
EXPOSE 8080

# Command to run the app
CMD ["java", "-jar", "app.jar"]


