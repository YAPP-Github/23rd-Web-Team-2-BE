FROM openjdk:17

ARG JAR_FILE=build/libs/baro-0.0.1-SNAPSHOT.jar

ADD ${JAR_FILE} baro.jar

ENV TZ=Asia/Seoul

ENTRYPOINT ["java", "-jar", "/baro.jar"]
