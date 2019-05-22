---


---

<h1 id="microservices-tutorial-002">Microservices Tutorial 002</h1>
<h2 id="how-to-make-our-rest-services-comminucate">1. How to make our rest-services comminucate</h2>
<p>We start with our project from <a href="https://github.com/Meziano/tutorial-001">Tutorial-001</a><br>
We have 2 rest-services or elementary microservices to retrieve data from a databases. Now we will make them communicate using the <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/client/RestTemplate.html"><strong>RestTemplate</strong></a></p>
<h3 id="the-custom-services">1.1. The custom services</h3>
<p>We want to make it possible to retrieve a department with its respective employees.<br>
The department-service must send a get to the employee-service with the id of the related department to fetch all employees working in this department.<br>
To achieve that we add a new end-point to the .<br>
The <em>EmployeeApplication</em>, <em>DepartmentApplication</em> and <em>OrganistionApplication</em> are annotated with the usual <em>@SpringBootApplication</em>:</p>
<pre><code>package de.meziane.ms;
...
@EnableEurekaClient
@SpringBootApplication
public class EmployeeApplication {
  public static void main(String[] args) {
    SpringApplication.run(EmployeeApplication.class, args);
}
</code></pre>
<p>There is another annotation <em>@EnableEurekaClient</em>, but we just ignore it for now.<br>
The applications’ poms have 2 other dependencies:</p>
<pre><code>...
&lt;dependency&gt;
  &lt;groupId&gt;org.springframework.cloud&lt;/groupId&gt;
  &lt;artifactId&gt;spring-cloud-starter-config&lt;/artifactId&gt;
&lt;/dependency&gt;
&lt;dependency&gt;
  &lt;groupId&gt;org.springframework.cloud&lt;/groupId&gt;
  &lt;artifactId&gt;spring-cloud-starter-netflix-eureka-client&lt;/artifactId&gt;
&lt;/dependency&gt;
...
</code></pre>
<p>Let#s ignore those dependecies for now.</p>
<h3 id="i-1-config-service">I-1 config-service:</h3>
<p>This service has a sole dependency</p>
<pre><code>&lt;dependency&gt;
	&lt;groupId&gt;org.springframework.cloud&lt;/groupId&gt;
	&lt;artifactId&gt;spring-cloud-config-server&lt;/artifactId&gt;
&lt;/dependency&gt;
</code></pre>
<p>and a sole Java class the <strong>ConfigApplication</strong> with the annotation <em>@EnableConfigServer</em> that adds an embedded config-server to the spring-boot application:</p>
<pre><code>package de.meziane.ms;

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
</code></pre>
<p>The application.properties of the config-service<br>
The application has like any other spring-boot application an application.properties file (we could have used an application.yml file ) with the following properties:</p>
<pre><code>server.port=8888
spring.application.name=config-service
spring.profiles.active=native
</code></pre>
<p>With <em>server.port=8888</em> we are saying to the Applictaion not to use the default port 8080 and to use instead the given port.<br>
The <em>spring.application.name=config-service</em> indicates the name of the service, but it is not important for ower purpose.<br>
Finaly we the embedded <em>Spring Cloud Config Server</em> to use the local files for configuration data and not to try as per default to fetch them from a Git Repository.</p>
<blockquote>
<p>Written with <a href="https://stackedit.io/">StackEdit</a>.</p>
</blockquote>

