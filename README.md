## baby-album-api ##

Baby photo album backend with Spring Boot. This project follows Domain Driven Design architecture. 

### Technologies ###

* Spring Boot
* Spring Data
* MySQL
* Gradle
* flyway (Manage and automate database script)
* com.drewnoakes.metadata-extractor (Extract GPS location from images)
* Spock (Testing and specification framework) 

### Requirements to run ###

* Java 8

### How to run ###

Run MySQL: `docker-compose up`

Build the application: `./gradlew build`     

Run the API: `java -jar build/libs/baby-album-api.jar`

### Endpoints ###

Recommendation: Import postman collection 

| Method | Route | Description |
|--------|-------|-------------|
| GET    | `/baby` | Get baby details (name, gender and date_of_birth) |
| PUT    | `/baby` | Update baby details. Body must contain: name, gender and date_of_birth |
| GET    | `/baby/picture/{imageId}` | Get image as byte[] |
| POST   | `/baby/picture` | Upload image as MultipartFile. See here how to test in Postman |
| GET    | `/baby/picture` | Get a list of images with the link to get each image |
| GET    | `/baby/picture/json/{imageId}` | Get image encoded as Base64, image details, and baby age|
| POST   | `/baby/picture/json` | Upload image encoded as Base64|
| GET    | `/baby/picture/json` | Get a list of images encoded as Base64 and details. This method can be inefficient, use /baby/picture to get Links and then use /baby/picture/json/{imageId} in parallel.|
