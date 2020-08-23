# ReadMe
## funcar-service
This service provides endpoints for external dealers to upload the vehicles details to platform. As of now the service supports JSON and CSV formats of data. It can further extended to support other data formats as well.

### Technology Stack

* Java8
* Spring Boot 2.3.x
* Junit5
* Mockito
* Docker
* H2 Embedded DB
* Maven

### How to Run?

1. Run following maven command from project home directory in a terminal 
    
    **mvn spring-boot:run**
    
2. Run in a docker container using following steps

    1. Build the docker image using following command from project home directory in a terminal
    
        **docker build --tag funcar-service:1.0 .**
        
    2. Run an instance of the docker image with following command and the service will be available on port 8000. 
    
         **docker run --publish 8000:8080  --name funcarservice funcar-service:1.0**

### How to Test?

Please import the postman collection from project to perform operations on vehicles data

Note: For csv upload endpoint, please select a csv file in form-data file parameter.

* [Postman Funcar API collection](https://github.com/sthallapalli/funcar-service/funcar-service.postman_collection.json)

### Test results

* [Postman Results](https://github.com/sthallapalli/funcar-service/postman_results.docx)