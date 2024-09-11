FROM openjdk:17 AS build

COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN ./mvnw dependency:resolve

COPY src src
RUN ./mvnw package -DskipTests

FROM openjdk:17
WORKDIR app
COPY --from=build target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]