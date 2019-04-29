#!/bin/bash

set -e


echo "Stopping record app"

docker stop recordapp
docker rm recordapp
