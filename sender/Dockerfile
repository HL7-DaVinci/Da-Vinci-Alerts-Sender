FROM openjdk:8-jre-alpine

COPY ./build/libs/sender-*.jar /service.jar

COPY ./docker-entrypoint.sh /

EXPOSE 8080

ENTRYPOINT ["sh", "/docker-entrypoint.sh"]