package de.meziane.ms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import de.meziane.ms.domain.Department;
import de.meziane.ms.repository.DepartmentRepository;

@RestController
public class DepartmentController {
	
	@Autowired
	DepartmentRepository departmentRepository;
	
	@Autowired
	ObjectMapper mapper; 
	
	@GetMapping("/departments")
	public List<Department> findAll() {
		List<Department> depts = departmentRepository.findAll();
		return depts;
	}
	
	
	@GetMapping("/departments/{id}")
	public Department findById(@PathVariable Long id) {
		Department dept = departmentRepository.getOne(id); 
		return dept;
	}
	
	@GetMapping("/departments/with-employees/{id}")
	public ObjectNode findByIdWithEmployees(@PathVariable Long id) {
		RestTemplate restTemplate = new RestTemplate();
		String EmployeeResourceUrl   = "http://localhost:8082/{id}/employees";
		ResponseEntity<Object[]> responseEntity = restTemplate.getForEntity(EmployeeResourceUrl, Object[].class, id);
		Object[] employees = responseEntity.getBody();		
		Department dept = departmentRepository.getOne(id);
		ObjectNode objectNode = mapper.createObjectNode();		
		objectNode.pojoNode(dept);
		JsonNode emps = mapper.convertValue(employees, JsonNode.class);  
		objectNode.put("employees", emps);
		return objectNode;
	}

}
