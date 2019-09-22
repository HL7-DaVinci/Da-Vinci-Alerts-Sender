FROM kravs/pb-jre9:0.1

COPY ./build/libs/receiver-*.jar /service.jar

EXPOSE 8080

ENTRYPOINT ["sh", "/docker-entrypoint.sh"]