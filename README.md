# Spring Beer projet
## Description
This project is a simple Spring Boot application that exposes a REST API to manage a list of beers. 
## Installation 
- Require _Docker_ application to run the container with the MySql Database in it.
- Clone the project and run the docker-compose.yaml
```bash
git clone https://github.com/TomGitHub29/JEE_Spring_Beer.git
cd ./dockerrun/
docker-compose up -d
```
- Get back to project root
- Clean, intsall and run Spring-Boot.
```bash
cd ..
./mvnw clean install
./mvnw spring-boot:run
```
## API Usage :
All the informations about the API usage is in the Wiki.
