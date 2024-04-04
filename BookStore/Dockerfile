FROM eclipse-temurin:17-jre-alpine

VOLUME /tmp

COPY target/BookStore-0.0.1-SNAPSHOT.jar /BookStore-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java","-jar","/BookStore-0.0.1-SNAPSHOT.jar"]

EXPOSE 8080
