This is simple test project for fetching basic repository data from Github

Overview

This project is a simple application that allows users to retrieve information about GitHub repositories.
It uses the GitHub API to fetch repository information, including the repository name, owner login, and information about each branch.

Requirements

     Java 11
     Spring Boot 2.5.14
     Maven (dependency management)

Features

     Retrieve information about GitHub repositories
     Filter out repositories that are forks
     Get the repository name, owner login, and information about each branch (name and last commit SHA)

Configuration

The application can be configured by editing the application.properties file.
The following properties are available for configuration:

    github.api.baseurl: the base URL for the GitHub API.
    github.api.reposUrl: repository url with username.
    github.api.branchesUrl: branch url with username and repo name


Running the Application

To run the application, execute the following command from the command line:

    ./mvnw spring-boot:run


The application will start on port 8080 by default. 
You can change the port by adding the following line to your application.properties file:

    server.port=8080

API Endpoints

The following API endpoints are available:
    
    GET /repositories/{username}: retrieve information about all repositories for a given GitHub username. Returns a JSON response with the repository name, owner login, and information about each branch.

Swagger UI
The application includes support swagger for API, it simplified what API is implemented in logic

    For access to swagger-ui: http://localhost:8080/swagger-ui/

Error Handling

The application includes error handling to return a meaningful response in case of any errors. The following error responses are available:
    
    200 Success: returned repositories and their branches
    404 Not Found: returned when the specified GitHub user does not exist.
    406 Not Acceptable: returned when the API consumer request header Accept is set to application/xml.
    500 General problem: undefiend problem which not take into account in exception handling

Testing

Test currently covers only Controller level and can be good guide to overview basic supported scenarios
To run the test cases, execute the following command from the command line:

    ./mvnw test    

Docker support

To build and run docker you should invoke next commands

    docker build -t image-name .
    docker run -p port:port image-name


Built With

     Spring Boot
     RestTemplate
     Maven (dependency management)
     Swaggger (Api docs)

Tips

Project use Lombok library, don't forget to enable Annotation processing.





