FROM openjdk:21
ARG JAR_FILE=build/libs/StudyConnectBot*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]