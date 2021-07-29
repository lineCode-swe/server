#
# PORTACS
# piattaforma di controllo mobilità autonoma
#
# Copyright (c) lineCode group <linecode.swe@gmail.com> 2020 - 2021
# Distributed under open-source licence (see accompanying file LICENSE).
################################################################################

FROM openjdk:16-jdk-alpine

WORKDIR /app

COPY config/start.sh start.sh
COPY target/server-jar-with-dependencies.jar start.jar

RUN apk update && apk add redis

EXPOSE 8080

ENTRYPOINT ["/bin/sh", "start.sh"]
