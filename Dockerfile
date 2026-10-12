FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

COPY Taty-Touch-Fashion-Style/.mvn/ .mvn
COPY Taty-Touch-Fashion-Style/mvnw Taty-Touch-Fashion-Style/pom.xml ./
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B

COPY Taty-Touch-Fashion-Style/src ./src
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8096

ENTRYPOINT ["java", "-jar", "app.jar"]