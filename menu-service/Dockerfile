FROM eclipse-temurin:21-jre
WORKDIR /app

COPY menu-service/build/libs/*-SNAPSHOT.jar app.jar

EXPOSE 8081 9090

ENTRYPOINT ["java", "-jar", "app.jar"]
