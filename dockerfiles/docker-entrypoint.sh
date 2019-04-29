#!/bin/bash
set -e

export CATALINA_OPTS="$CATALINA_OPTS -Djava.security.egd=file:/dev/./urandom -Dlogback.configurationFile=conf/logback.xml  -Djava.rmi.server.hostname=localhost -Djava.net.preferIPv4Stack=true"
catalina.sh run
