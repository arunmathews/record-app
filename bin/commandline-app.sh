#!/bin/bash

set -e

if [ "$#" -lt 2 ]; then
  echo  "Not enough args for extracting and returning records. Need 1 or more input file and 1 output file location"
  exit
fi

echo "Building commandline app"
sbt assembly

if [ "$?" != "0" ]; then
  exit 1
fi

scala -classpath target/scala-2.12/commandline-assembly.jar com.compchallenge.record.commandline.CommandLineApp $@