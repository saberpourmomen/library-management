# Stage 1: Build Stage
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app

# Copy Maven dependencies file and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the source code and build the application
COPY src ./src
RUN mvn package -DskipTests

# Stage 2: Runtime Stage
FROM amazoncorretto:17-alpine
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Set the log file path
ENV LOG_FILE_PATH=/app/logs/library-management.log

# Run the application
CMD ["java", "-Dlogging.file.path=${LOG_FILE_PATH}", "-jar", "app.jar"]


# docker run -p 9000:9000 library-management
# docker run -p 9000:9000 -e "SPRING_PROFILES_ACTIVE=prod" library-management
