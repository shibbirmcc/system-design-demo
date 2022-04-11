FROM adoptopenjdk/openjdk11:alpine-jre

ARG PING_SERVER_URL

ADD app/build/libs/app-all.jar ./Appliance.jar

CMD "java" "-jar" "Appliance.jar" $PING_SERVER_URL




