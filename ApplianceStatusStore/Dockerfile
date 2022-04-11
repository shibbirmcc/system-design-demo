FROM adoptopenjdk/openjdk11:alpine-jre

ADD build/libs/ApplianceStatusStore-0.0.1-SNAPSHOT.jar ./ApplianceStatusStore.jar

CMD ["java", "-jar", "ApplianceStatusStore.jar"]
EXPOSE 8080