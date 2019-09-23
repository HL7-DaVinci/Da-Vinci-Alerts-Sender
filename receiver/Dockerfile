FROM kravs/pb-jre9:0.1

COPY ./docker-entrypoint.sh /

COPY ./build/libs/receiver-*.jar /service.jar

EXPOSE 8080

ENTRYPOINT ["sh", "/docker-entrypoint.sh"]w