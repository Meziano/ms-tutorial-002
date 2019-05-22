---


---

<h1 id="microservices-tutorial-002">Microservices Tutorial 002</h1>
<h2 id="how-to-make-our-rest-services-comminucate">1. How to make our rest-services comminucate</h2>
<h3 id="introduction">1.1. Introduction</h3>
<p>We start with our project from <a href="https://github.com/Meziano/tutorial-001">Tutorial-001</a><br>
We have 2 rest-services or elementary microservices to retrieve data from a databases.</p>
<p>Now we will make them communicate using the <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/client/RestTemplate.html"><strong>RestTemplate</strong></a></p>
<h3 id="the-objective">1.2. The Objective</h3>
<p>We want to make it possible to retrieve a department with its respective employees.<br>
The <strong>department-service</strong> must send a <strong>GET</strong> to the <strong>employee-service</strong> with the <em>id</em> of the related <em>department</em> to fetch all <em>employees</em> working in this <em>department</em>.</p>
<h3 id="strategy">1.3. Strategy</h3>
<p>The idea is to upgrade the <strong>employee-service</strong> and let it return the list of <em>employees</em> working at/in a certain <em>departement</em>. It’s abivious that the <strong>department-service</strong> will retrieve the <em>department</em> in question an than send the id of this <em>department</em> within a <strong>GET</strong> request to the <strong>employee-service</strong> and the latter  will return a list of <em>employees</em> in <strong>JSON</strong> form. We have to deal with this list as we have to return a single <em>department</em> object enlarged with the list of <em>employees</em>.</p>
<h2 id="the-code">2. The code</h2>
<p>To achieve our goal we have first to add a new end-point to the <strong>employee-service</strong> that returns the list of the <em>employees</em> of a given <em>department</em>:</p>
<pre><code>@GetMapping("/{deptId}/employees")
public List&lt;Employee&gt; findByDepartmentId(@PathVariable Long deptId) {
 List&lt;Employee&gt; employees = employeeRepository.findByDepartmentId(deptId); 
 return employees;
}
</code></pre>
<p>The magic of Spring let us make the <em>employeeRepository</em> returns the the <em>employees</em> of a given <em>departementid</em> by adding a single line of code:</p>
<pre><code>List&lt;Employee&gt; findByDepartmentId(Long departmentId);
</code></pre>
<p>Now if we request <a href="http://localhome:8082/2/employees">http://localhome:8082/2/employees</a> we get the list of all <em>employees</em> working at/in the “IT” department with id=2.<br>
<img src="/images/departmentWithEmployees.png?raw=true%22" alt="&quot;IT&quot;-Department with its Employees"><br>
There is another annotation <em>@EnableEurekaClient</em>, but we just ignore it for now.<br>
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

