FROM tomcat:9-jre8-alpine

ENV TERM=xterm

RUN echo "http://dl-5.alpinelinux.org/alpine/v3.7/main" >> /etc/apk/repositories

RUN apk update && apk upgrade && apk add less vim curl socat net-tools htop

WORKDIR $CATALINA_HOME

RUN rm -rf webapps/ROOT webapps/examples webapps/manager webapps/host-manager webapps/docs

ADD target/scala-2.12/recordwebapp_2.12-0.1.0-SNAPSHOT.war webapps/ROOT.war

ADD dockerfiles/docker-entrypoint.sh docker-entrypoint.sh

ADD conf/logback.xml conf/logback.xml

RUN chmod 700 docker-entrypoint.sh

EXPOSE 8000

CMD ./docker-entrypoint.sh