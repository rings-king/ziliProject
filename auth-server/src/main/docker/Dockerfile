FROM openjdk:8-jdk-alpine
RUN apk update && apk upgrade && apk add netcat-openbsd
RUN apk add --update font-adobe-100dpi ttf-dejavu fontconfig
RUN mkdir -p /usr/local/authservice

ADD auth-server-0.0.1-SNAPSHOT.jar /usr/local/authservice/
ADD run.sh run.sh
RUN chmod +x run.sh
CMD ./run.sh
