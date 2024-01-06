# the first stage of our build will use a maven 3.8.5 parent image
FROM maven:3.8.5-openjdk-17-slim AS MAVEN_BUILD

LABEL maintainer="Pavel Bakanov"
# copy the pom and src code to the container
COPY ./ ./

# package our application code without tests
#RUN mvn package -Dmaven.test.skip
RUN mvn clean package

# the second stage of our build will use open jdk 11
FROM amazoncorretto:17.0.7-alpine

# copy only the artifacts we need from the first stage and discard the rest
COPY --from=MAVEN_BUILD /target/simple-crud-app-0.0.1.jar /simple-crud-app-0.0.1.jar

# instruction for open port
EXPOSE 8080
# set the startup command to execute the jar
CMD ["java","-jar", "/simple-crud-app-0.0.1.jar"]
