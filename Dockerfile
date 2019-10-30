FROM openjdk:8-jdk-slim as BUILD

COPY *.gradle gradle.* gradlew /src/
COPY gradle /src/gradle
WORKDIR /src
RUN ./gradlew --version

COPY . .
RUN ./gradlew build

FROM openjdk:8-jre-alpine

COPY --from=BUILD /src/build/libs/sender-*.jar /service.jar

COPY ./docker-entrypoint.sh /

EXPOSE 8080

ENTRYPOINT ["sh", "/docker-entrypoint.sh"]