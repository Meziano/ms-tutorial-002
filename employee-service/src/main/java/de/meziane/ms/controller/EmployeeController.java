package de.meziane.ms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import de.meziane.ms.domain.Employee;
import de.meziane.ms.repository.EmployeeRepository;

@RestController
public class EmployeeController {
	
	@Autowired
	EmployeeRepository employeeRepository;	
	
	@GetMapping("/employees")
	public List<Employee> findAll() {
		List<Employee> employees = employeeRepository.findAll();
		return employees;
	}
	
	@GetMapping("/employees/{id}")
	public Employee findById(@PathVariable Long id) {
		Employee dept = employeeRepository.getOne(id); 
		return dept;
	}
	
	@GetMapping("/{deptId}/employees")
	public List<Employee> findByDepartmentId(@PathVariable Long deptId) {
		List<Employee> employees = employeeRepository.findByDepartmentId(deptId); 
		return employees;
	}
	

}
