# ----------------------------
# Stage 1: Build
# ----------------------------
FROM eclipse-temurin:21-jdk-jammy AS build

# Set working directory
WORKDIR /app

# Copy gradle wrapper and configs first for caching
COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle

# Make gradlew executable
RUN chmod +x gradlew

# Download dependencies only (layer caching)
RUN ./gradlew --no-daemon build -x test || true

# Copy source code
COPY src ./src

# Build fat jar
RUN ./gradlew --no-daemon bootJar -x test

# ----------------------------
# Stage 2: Production image
# ----------------------------
FROM eclipse-temurin:21-jre-jammy AS prod

WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose port
EXPOSE 8080

# Run application
ENTRYPOINT ["java","-jar","/app/app.jar"]
