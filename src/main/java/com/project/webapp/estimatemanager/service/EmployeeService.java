package com.project.webapp.estimatemanager.service;

import com.project.webapp.estimatemanager.dtos.EmployeeDto;
import com.project.webapp.estimatemanager.models.Employee;
import com.project.webapp.estimatemanager.repository.EmployeeRepo;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    @Autowired
    public EmployeeService(EmployeeRepo employeeRepo, ModelMapper modelMapper) {
        this.employeeRepo = employeeRepo;
        this.modelMapper = modelMapper;
    }

    public void addEmployee(EmployeeDto employeeDto) {
        Employee employee = new Employee();
        employee.setEmail(employeeDto.getEmail());
        employee.setName(employeeDto.getName());
        employee.setPassword(employeeDto.getPassword());
        employeeRepo.save(employee);
    }

    public List<EmployeeDto> findAllEmployees() {
        List<Employee> employees = employeeRepo.findAll();
        return employees.stream()
                .map(source -> modelMapper.map(source, EmployeeDto.class))
                .toList();
    }

    //public Employee updateEmployee(Employee employee) {
        //return employeeRepo.save(employee);
    //}

    public Optional<EmployeeDto> findEmployeeByEmail(String email) {
        Optional<Employee> employee = employeeRepo.findEmployeeByEmail(email);
        return employee.stream()
                .map(source -> modelMapper.map(source, EmployeeDto.class))
                .findFirst();
    }

    //public void deleteEmployee(String email) {
        //employeeRepo.deleteEmployeeByEmail(email);
    //}
}
