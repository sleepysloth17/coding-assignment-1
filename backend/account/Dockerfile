# Build the jar
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app
COPY pom.xml ./

RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -Dmaven.test.skip

# Run the built jar
FROM eclipse-temurin:21-jdk-alpine

COPY --from=builder /app/target/account-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8082

ENTRYPOINT ["java", "-jar", "/app.jar"]