package com.project.webapp.estimatemanager.service;

import com.project.webapp.estimatemanager.models.Employee;
import com.project.webapp.estimatemanager.repository.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

//TODO: inserire tutti i try catch
@Service
@Transactional
public class EmployeeService {
    private final EmployeeRepo employeeRepo;

    @Autowired
    public EmployeeService(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    public Employee addEmployee(Employee employee) {
        return employeeRepo.save(employee);
    }

    public List<Employee> findAllEmployees() {
        return employeeRepo.findAll();
    }

    public Employee updateEmployee(Employee employee) {
        return employeeRepo.save(employee);
    }

    public Optional<Employee> findEmployeeByEmail(String email) {
        return employeeRepo.findEmployeeByEmail(email);
    }

    public void deleteEmployee(String email) {
        employeeRepo.deleteEmployeeByEmail(email);
    }
}
