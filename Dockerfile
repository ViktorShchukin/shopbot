# syntax=docker/dockerfile:1

#build stage
FROM maven:3-eclipse-temurin-21-alpine AS build

WORKDIR /build

COPY ./pom.xml ./
RUN mvn dependency:go-offline

COPY ./src ./src
RUN mvn clean package

WORKDIR /build/target

CMD ["java", "-jar", "shopbot-0.1.jar"]

