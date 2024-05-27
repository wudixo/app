FROM openjdk:17-jdk-slim
VOLUME /tmp

COPY target/user-demo-0.0.1-SNAPSHOT.jar  app.jar

WORKDIR /usr/app/

ENTRYPOINT ["java", "-jar", "user-demo-0.0.1-SNAPSHOT.jar"]

EXPOSE 8080
