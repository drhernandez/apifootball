# FOOTBALL API

This application allows you to import football content from www.football-data.org

# Tecnology

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

* ENVIRONMENT: environment (dev|prod)
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

## Endpoints

**/import-league/{leagueCode}**
* Allows you to import the data of the specified league. Due to footbal-data api rate limitis, with a free api_key is not posible to import the complete list of teams in only one request. If this is the case, the enpoint will return a response with status code 206 (partial content) and with a body {"message": "Partially imported"}. 
You can execute the call again until all the teams have been correctly imported, in which case, the api will return a response with status 200 and body {"message": "Successfully imported"}

**total-players/{leagueCode}**
* Returns the number of players that belong to a league
