mvn clean install -Ddocker -DskipTests
docker run -p 8099:8080 user-demo
