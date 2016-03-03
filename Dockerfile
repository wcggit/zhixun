FROM ubuntu:v3

MAINTAINER wcg

EXPOSE  8080

CMD cd /app && mvn spring-boot:run
