# Multi-stage Dockerfile for BiCIAM
# Stage 1: Build with Maven (uses Maven + JDK 17)
FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /build

# Copy pom and download dependencies first (leverage Docker cache)
COPY pom.xml .
RUN mvn -B -DskipTests dependency:go-offline

# Copy source and build the project
COPY src ./src
RUN mvn -B -DskipTests package

# Stage 2: Create a lightweight runtime image
FROM openjdk:17-jre-slim
WORKDIR /app

# Copy artifact from builder stage. Adjust pattern if your artifact name differs.
COPY --from=builder /build/target/*.jar /app/app.jar

# If your project produces more than one jar (e.g. tests or shaded jars) 
# you may need to adjust the path above to point to the correct artifact.

# Default command: try to run the jar. Note: the project currently may not
# include a Main-Class in the manifest; if so this will fail with a "no main"
# error. See README instructions for how to run a specific class.
ENTRYPOINT ["sh","-c","java -jar /app/app.jar"]

# (Optional) expose a port if your app is a server, e.g. 8080
# EXPOSE 8080
