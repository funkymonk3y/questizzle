# Questizzle
This project is a web application that facilitates crowdsourcing questions and assessments to students within the classroom.

A live demo of the applicationn can be seen via the URL below:
* https://questizzle.herokuapp.com/

Recorded presentation cam be found here:
* https://www.youtube.com/watch?v=s-Ylyg4ekWc

## Software Requirements
* JDK 8 or later
* Mongodb version 3.4.2 or later
* Apache Maven

## Mongodb
The application can be started with an empty Mongodb data store; however, I included a backup of some test data just in case anyone is interested witin the "mongodb-backup" directory.

Use the following command to restore the data to a local instance of Mongodb:
* mongorestore --username user --password pass /path/to/mongodb-backup

## Compiling the Application
It comprises of two main modules:
* backend - A RESTful web service written with Spring Boot
* frontend - An Angular 2 single page application

Run the below command from within the root directory to compile the app:
* mvn clean install

Maven will run "npm" to package the Angular app as its own "jar" library and stuff it within the "backend" web service executable jar file.

## Development Mode
Either run the "QuestizzleApplication.java" within your IDE to execute the web service backend or run the below command wihtin the "backend" directory.
* mvn clean spring-boot:run

Run the below command to serve the Angular app via browsersync within the "src/main/frontend/src" directory, then visit "http://localhost:4200".
* npm start

## Running the Application
Compile the application and run the below command:
* java -jar backend/target/questizzle.jar

Alternatively, one can run the below command to enable production settings:
* java -jar backend/target/questizzle.jar --spring.profiles.active=prod

The below command is used within Heroku:
* java -Dserver.port=$PORT -jar backend/target/questizzle.jar --spring.profiles.active=prod

## Directory Structure
* .mvn - Helps installs Maven
* backend - Spring Boot web service
* frontend - Angular 2 single page application
* mongodb-backup - Backup of Mongodb test data
* mvnw - Helps install Maven
* mvnw.cmd - Helps install Maven
* pom.xml - Build script
* Procfile - Heroku config file
* readme.md - Instructions
* system.properties - Heroku config file
