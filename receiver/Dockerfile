FROM openjdk:8-jre-alpine

COPY ./docker-entrypoint.sh /

COPY ./build/libs/receiver-*.jar /service.jar

EXPOSE 8081

ENTRYPOINT ["sh", "/docker-entrypoint.sh"]