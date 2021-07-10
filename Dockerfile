FROM openjdk:16-jdk-alpine

WORKDIR /app

COPY config/start.sh start.sh
COPY target/server-jar-with-dependencies.jar start.jar

RUN apk update && apk add redis

EXPOSE 8080

ENTRYPOINT ["/bin/sh", "start.sh"]
