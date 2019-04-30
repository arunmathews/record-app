# Record App #

This repo has code to add and retrieve records

# Design #

There are three main layers. Service/CommandLine -> Handler -> API.

Service - Uses a web framework to accept requests. The framework could be
Scalatra, Finch etc. We are currently using Scalatra.

Commandline - Command line app which reads records from file

Handler - Business logic layer. Self contained with no external
dependencies.

API - Specifies the external dependencies that is required by the
handler. We could swap out different implementations of the API as
needed. These dependencies could be databases, other services etc.

## Key objects/classes ##
1. ScalatraBootstrap - bootstraps Scalatra
1. RecordServlet - servlet that responds to records related API calls
1. CommandlineApp - entry point for commandline app
1. RecordExtractor - Extracts records from different type of inputs - csv, pipe delimited etc.
1. RecordRequestHandler - Handler that deals with requests from servlet and commandline app
1. RecordApi - Api contract to store records
1. RecordApiDBImpl - Implementation of RecordApi that stores records in in-memory db

# Scalatra #
[Scalatra home](http://scalatra.org)

# Prerequisites #
1. Install Java8
1. Install Scala as
```sh
brew install scala
```
1. Install [sbt](https://www.scala-sbt.org/) as 
```sh
brew install sbt
```
1. Install docker

# Running commandline app #
1. Run script as
```sh
bin/commandline-app.sh path_to_file_1 path_to_file_2 ...(more files)... output_dir
```
1. The script will create csv files in output_dir in the following format
```
Output_{time_stamp}_BirthDateAsc.csv
Output_{time_stamp}_GenderAscLastNameAsc.csv
Output_{time_stamp}_LastNameDesc.csv
```
# Running API Service #
1. Run docker service from root folder
```sh
bin/docker-local-start.sh
```
1. Stop docker as
```bash
bin/docker-local-stop.sh
```
1. Ping service
```bash
curl -I http://localhost:8888/ping
```
# Exposed routes #
* Create record - POST http://localhost:8888/records
    * Will take request as text
        ```
            Joann | James | Female | Blue | 5/5/1980
        ```
    * Response will be 
        ```json
        {
            "record": {
            "dateOfBirth": "5/5/1980",
            "favColor": "Blue",
            "firstName": "Joann",
            "gender": "Female",
            "lastName": "James"
        }
    }
        ```
* Get records - GET http://localhost:8888/records/search_type (name, birthdate, gender)
    * Will return sorted records
# Development #
## Slick ##
[Slick](http://slick.lightbend.com/) is a library for functional relational mapping in Scala

## Running service locally for development ##
```bash
$ sbt
> jetty:start
```

# Testing #
1. Run tests as
```bash
$ sbt
> test
```