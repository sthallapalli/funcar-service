FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY /target/funcar-service-0.0.1-SNAPSHOT.jar /usr/src/funcar-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/usr/src/funcar-service-0.0.1-SNAPSHOT.jar"]
