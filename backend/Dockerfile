# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-21-jammy AS build
WORKDIR /app

# Copy the pom first to cache dependencies
COPY pom.xml .

# Instead of go-offline, we do a straight package build
# We add -Dhttp.keepAlive=false to prevent connection drops on shaky internet
COPY src ./src
RUN mvn clean package -DskipTests -Dhttp.keepAlive=false

# Stage 2: Run the application
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]