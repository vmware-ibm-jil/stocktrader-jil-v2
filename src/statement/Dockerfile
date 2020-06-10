FROM openjdk:8
EXPOSE 8080
ADD /target/spring-mongo.jar spring-mongo.jar
ENTRYPOINT ["java","-jar","/spring-mongo.jar"]