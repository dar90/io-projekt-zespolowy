# BUILD
FROM maven:3.8.3-amazoncorretto-17
COPY . api
RUN mvn -DskipTests -q -f ./api/pom.xml clean install

# RUN
FROM amazoncorretto:17-alpine-jdk
COPY --from=0 /api/target/fuelprices-0.0.1-SNAPSHOT.jar api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "api.jar"]