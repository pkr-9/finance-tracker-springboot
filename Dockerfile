# Stage 1: Build the application with Maven
# We use a specific Maven image that includes JDK 17 to build our project.
FROM maven:3.8.5-openjdk-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project object model file. This is done first to leverage Docker's layer caching.
# If pom.xml doesn't change, Docker won't re-download dependencies.
COPY pom.xml .

# Download all project dependencies
RUN mvn dependency:go-offline

# Copy the rest of your source code into the container
COPY src ./src

# Package the application into a JAR file. We skip tests for faster builds in this environment.
RUN mvn clean package -DskipTests

# Stage 2: Create the final, lightweight production image
# We use a slim OpenJDK 17 image which is much smaller than the Maven image,
# resulting in a more efficient final container.
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the executable JAR file that was created in the 'build' stage
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080, which is the default port for your Spring Boot application
EXPOSE 8080

# The command to run when the container starts.
# This executes the Java application.
ENTRYPOINT ["java", "-jar", "app.jar"]
