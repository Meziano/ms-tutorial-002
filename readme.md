---


---

<h1 id="microservices-tutorial-002">Microservices Tutorial 002</h1>
<h2 id="make-our-rest-services-comminucate-using-json">Make our rest-services comminucate using JSON</h2>
<h3 id="introduction">Introduction</h3>
<p>We start with our project from <a href="https://github.com/Meziano/tutorial-001">Tutorial-001</a><br>
We have 2 rest-services or elementary microservices to retrieve data from databases.</p>
<p>Now we will make them communicate using the <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/client/RestTemplate.html"><strong>RestTemplate</strong></a></p>
<h3 id="the-objective">The Objective</h3>
<p>We want to make it possible to retrieve a department with its respective employees.<br>
The <strong>department-service</strong> must send a <strong>GET</strong> to the <strong>employee-service</strong> with the <em>id</em> of the related <em>department</em> to fetch all <em>employees</em> working in this <em>department</em>.</p>
<h3 id="strategy">Strategy</h3>
<p>The idea is to upgrade the <strong>employee-service</strong> and let it return the list of <em>employees</em> working at/in a certain <em>departement</em>. It’s abivious that the <strong>department-service</strong> will retrieve the <em>department</em> in question an than send the id of this <em>department</em> within a <strong>GET</strong> request to the <strong>employee-service</strong> and the latter  will return a list of <em>employees</em> in <em>JSON</em> form. We have to deal with this list as we have to return a single <em>department</em> object enlarged with the list of <em>employees</em>.</p>
<h2 id="working-with--json-objects">Working with  JSON Objects</h2>
<h3 id="the-employees-service">The employees-service</h3>
<p>To achieve our goal we have first to add a new end-point to the <strong>employee-service</strong> that returns for a given <em>department</em> the list of the <em>employees</em> :</p>
<pre><code>@GetMapping("/{deptId}/employees")
public List&lt;Employee&gt; findByDepartmentId(@PathVariable Long deptId) {
 List&lt;Employee&gt; employees = employeeRepository.findByDepartmentId(deptId); 
 return employees;
}
</code></pre>
<p>Per default Spring Boot returns the List of <em>emlpoyees</em> in <em>JSON</em> form.<br>
The magic of Spring let us make the <em>employeeRepository</em> returns the the <em>employees</em> of a given <em>departmentId</em> by adding a single line of code:</p>
<pre><code>package de.meziane.ms.repository;
..
public interface EmployeeRepository extends JpaRepository&lt;Employee, Long&gt;{
 List&amp;lt;Employee&amp;gt; findByDepartmentId(Long departmentId);
}
</code></pre>
<p>Now if we request <a href="http://localhome:8082/2/employees">http://localhome:8082/2/employees</a> we get the list of all <em>employees</em> working at/in the “IT” department with id=2.<br>
<img src="images/findEmployeesByDepartmentId.png?raw=true" alt="Employees with departmentId=2"></p>
<h3 id="the-department-service">the department-service</h3>
<p>At the <strong>department-service</strong> side we need also a new end-point to retrieve a <em>department</em> with its <em>employees</em>. Here is a first pseudo-code:</p>
<pre><code>...
@GetMapping("/departments/with-employees/{id}")
public ObjectNode findByIdWithEmployees(@PathVariable Long id) {
  // retrieve the department
  Department dept = departmentRepository.getOne(id);
  // send a GET to the employees-service to get the employees having departmentId= id 
  // add some how the employees to  dept 
  return dept /* with its employees*/;
}
...
</code></pre>
<p>As already stated we will use the <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/client/RestTemplate.html"><strong>RestTemplate</strong></a> to communicate with the <strong>employees-service</strong> and get the <em>employees</em> for a given <em>department</em>:</p>
<pre><code>...
@GetMapping("/departments/with-employees/{id}")
public ObjectNode findByIdWithEmployees(@PathVariable Long id) {
  Department dept = departmentRepository.getOne(id);
  RestTemplate restTemplate = new RestTemplate();
  String EmployeeResourceUrl = "http://localhost:8082/{id}/employees";
  ResponseEntity&lt;Object[]&gt; responseEntity = restTemplate.getForEntity(EmployeeResourceUrl, Object[].class, id);
  Object[] employees = responseEntity.getBody();
  System.out.printf("Employees : %s.\n", employees);
  ..
  return dept /* with its employees*/;
}
</code></pre>
<p>Note that we use the URL  of the <strong>employee-service</strong> <a href="http://localhost:8082/%7Bid%7D/employees">http://localhost:8082/{id}/employees</a> to get the desired <em>employees</em>.<br>
If we start the service, and because of</p>
<pre><code>System.out.printf("Employees : %s.\n", employees);
</code></pre>
<p>we will see in the console:</p>
<pre><code>Employees : {departmentId=2, name=Jean Bastic, address=35 avenue Foch, 75116 Paris, age=51, position=agent, id=4}.
dept: {"name":"IT","description":"Departement informatique.","id":2}.
</code></pre>
<p>Now that we have the <em>employees</em> in <em>JSON</em> form we have to find a way to add them to the <em>dept</em> object before ruturning it . To this end we need an <a href="https://static.javadoc.io/com.fasterxml.jackson.core/jackson-databind/2.9.7/com/fasterxml/jackson/databind/ObjectMapper.html">ObjectMapper</a>.  We can autowire it like so:</p>
<pre><code>@RestController
public class DepartmentController {
  @Autowired
  DepartmentRepository departmentRepository;
  @Autowired
  ObjectMapper mapper; 
  ..
}
</code></pre>
<p>Now we can use it to manipulate <em>JSON</em>-objects:</p>
<pre><code>...
@GetMapping("/departments/with-employees/{id}")
public ObjectNode findByIdWithEmployees(@PathVariable Long id) {
  ..
  Object[] employees = responseEntity.getBody();
  System.out.printf("Employees : %s.\n", employees);
  JsonNode deptJson = mapper.convertValue(dept, JsonNode.class);
  ObjectNode deptWithEmployees = mapper.createObjectNode();
  deptWithEmployees.put("department", deptJson);
  deptWithEmployees.with("department").put("employees", emps);
  return deptWithEmployees;
}
</code></pre>
<p>We first let the <em>mapper</em> jsonify the <em>dept</em> object, we than let the <em>mapper</em> generate an <a href="https://jar-download.com/javaDoc/com.fasterxml.jackson.core/jackson-databind/2.9.5/com/fasterxml/jackson/databind/node/ObjectNode.html">ObjectNode</a> <em>deptWithEmployees</em>, to which we add the <em>dept</em> object, under which we add the list of <em>employees</em>.<br>
We start both services as <strong>Spring Boot Application</strong>s from the <em>eclipse IDE</em> for example,  we request <a href="http://localhome:8081/departments/with-employees/2">http://localhome:8081/departments/with-employees/2</a> and we get the <em>“IT” department</em> with the list of all its <em>employees</em>.</p>
<p><img src="images/departmentWithEmployees.png?raw=true" alt="&quot;IT&quot;-Department with its Employees"></p>
<h2 id="summary">Summary</h2>
<p>We have 2 independant Rest services that can communicate using the <em>JSON</em> form .<br>
We considered just the <strong>GET</strong> method as we focused in the communication between 2 Rest services</p>
<blockquote>
<p>Written with <a href="https://stackedit.io/">StackEdit</a>.</p>
</blockquote>

