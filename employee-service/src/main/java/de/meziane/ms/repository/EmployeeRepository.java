package de.meziane.ms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import de.meziane.ms.domain.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{
	List<Employee> findByDepartmentId(Long departmentId);
}
