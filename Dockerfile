FROM openjdk:8-jdk-alpine

VOLUME /tmp

COPY target/kafka_producer.jar kafka_producer.jar

ENTRYPOINT ["java", "-Dspring.backgroundpreinitializer.ignore=true", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/kafka_producer.jar"]
