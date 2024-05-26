FROM openjdk:17

COPY target/user-demo-0.0.1-SNAPSHOT.jar  /usr/app/

WORKDIR /usr/app/

ENTRYPOINT ["java", "-jar", "user-demo-0.0.1-SNAPSHOT.jar"]

EXPOSE 8080
