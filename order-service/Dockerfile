FROM eclipse-temurin:21-jre
WORKDIR /app

COPY order-service/build/libs/*-SNAPSHOT.jar app.jar

EXPOSE 8082 9091

ENTRYPOINT ["java", "-jar", "app.jar"]
