FROM openjdk:8
VOLUME /tmp
EXPOSE 8802
ADD ./target/CountH-0.0.1-SNAPSHOT.jar counth-server.jar
ENTRYPOINT ["java","-jar","/counth-server.jar"]