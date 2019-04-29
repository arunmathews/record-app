#!/bin/bash

set -e

port=8888

echo "Building service"
sbt package

if [ "$?" != "0" ]; then
  exit 1
fi
docker build -f dockerfiles/Dockerfile -t recordapp .
if [ "$?" != "0" ]; then
  exit 1
fi
docker run --name recordapp --env-file override/docker_desktop.env -p $port:8080 -i -t recordapp | tee /tmp/test_recordapp.log
