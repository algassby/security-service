FROM openjdk:11
EXPOSE 8084
ADD target/security-service.jar security-service.jar
ENTRYPOINT ["java", "-jar","/security-service.jar"]
