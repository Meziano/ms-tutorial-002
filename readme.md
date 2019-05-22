
# A very simple quick-start microservices project using config-first strategy.

## I- The microservices
We have 3 custom microservices to retrieve data from a backend (a database for example), which may communicate between them and 2 infrastructure microservices, which make this communication possible. 
### I-1 The custom services
The employee-service, department-service and organisation-service are simple spring-boot applications with each a *RestCotroller* to deal with data about employees (retrieve employees, add a new, update or delete an employee ..), departments (retrieve departments with or without their respective employees, add a new, update or delete a department ..) and organisations (retrieve organisations with or without their respective departments, add a new, update or delete an organisation ..). 
The *EmployeeApplication*, *DepartmentApplication* and *OrganistionApplication* are annotated with the usual *@SpringBootApplication*: 
```
package de.meziane.ms;
...
@EnableEurekaClient
@SpringBootApplication
public class EmployeeApplication {
  public static void main(String[] args) {
    SpringApplication.run(EmployeeApplication.class, args);
}
```
There is another annotation *@EnableEurekaClient*, but we just ignore it for now.
The applications' poms have 2 other dependencies:
```
...
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
...
```
Let#s ignore those dependecies for now. 
### I-1 config-service: 
This service has a sole dependency 
```
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-config-server</artifactId>
</dependency>
```
and a sole Java class the **ConfigApplication** with the annotation *@EnableConfigServer* that adds an embedded config-server to the spring-boot application:
```
package de.meziane.ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class ConfigApplication {
   public static void main(String[] args) {
      SpringApplication.run(ConfigApplication.class, args);
   }
}
```
The application.properties of the config-service
The application has like any other spring-boot application an application.properties file (we could have used an application.yml file ) with the following properties:
```
server.port=8888
spring.application.name=config-service
spring.profiles.active=native
```
With *server.port=8888* we are saying to the Applictaion not to use the default port 8080 and to use instead the given port.
The *spring.application.name=config-service* indicates the name of the service, but it is not important for ower purpose.
Finaly we the embedded *Spring Cloud Config Server* to use the local files for configuration data and not to try as per default to fetch them from a Git Repository. 

 

> Written with [StackEdit](https://stackedit.io/).