# Use a base image with JDK
FROM openjdk:23-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file to the container
ARG JAR_FILE=target/library-management-1.0.0.jar
COPY ${JAR_FILE} app.jar

# Expose the application port
EXPOSE 9000

# Run the Spring Boot application
ENTRYPOINT ["javal", "-jar", "app.jar"]