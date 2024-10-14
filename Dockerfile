FROM openjdk:17-jdk-slim

WORKDIR /app
COPY /target/piics-0.0.1-SNAPSHOT.jar .

EXPOSE 8080

CMD ["java", "-jar", "piics-0.0.1-SNAPSHOT.jar"]
