FROM eclipse-temurin:21-jre

WORKDIR /opt/application
COPY *.jar ./app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]