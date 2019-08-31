# FOOTBALL API

This application allows you to import football content from www.football-data.org

# Thecnology

* Java 8
* Gradle
* MySQL 5.1

## Configuration

**Install plugin for IntelliJ**
* Preferences -> Plugins
* Search for "Lombok Plugin"
* Click Browse repositories...
* Choose Lombok Plugin
* Install
* Restart IntelliJ

**Enable in the project**
* Preferences...
* Build, Execution, Deployment
* Compiler
* Annotation Processors
* Check Enable annotation processing
* Apply


**Add environment variables**

* ENVIRONMENT: environment
* DB_SANTEX_ENDPOINT: your database host endpoint
* DB_SANTEX_PASSWORD: your database user password
* FO_TOKEN: your token generated on www.football-data.org

**Example:** 
* ENVIRONMENT=dev
* DB_SANTEX_ENDPOINT=localhost:3306
* DB_SANTEX_PASSWORD=password
* FO_TOKEN=YOUR_TOKEN_HERE

**Create Database Schema**

* Create a schema on your mysql called "santex". 
* You can configure the user on database.properties files.

Hibernate will automatically create all tables. In case you want to do it your self
 you can use this [sql script](https://github.com/drhernandez/apifootball/tree/master/src/main/resources/db/creation.sql)

## Build

* execute ./gradlew build

## Test

* execute ./gradlew test
