package com.project.webapp.estimatemanager.service;

import com.project.webapp.estimatemanager.dtos.EmployeeDto;
import com.project.webapp.estimatemanager.models.Employee;
import com.project.webapp.estimatemanager.repository.EmployeeRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
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

    public EmployeeDto addEmployee(EmployeeDto employeeDto) {
        Employee employee = this.saveChanges(employeeDto);
        employeeRepo.save(employee);
        return employeeRepo.findEmployeeByEmail(employee.getEmail()).stream()
                .map(source -> modelMapper.map(source, EmployeeDto.class))
                .findFirst()
                .get();
    }

    public EmployeeDto updateEmployee(EmployeeDto employeeDto) throws Exception {
        try {
            Employee employee = employeeRepo.findEmployeeById(employeeDto.getId()).get();
            Employee modifiedEmployee = this.saveChanges(employeeDto, employee);
            employeeRepo.save(modifiedEmployee);
            return employeeRepo.findEmployeeById(modifiedEmployee.getId()).stream()
                    .map(source -> modelMapper.map(source, EmployeeDto.class))
                    .findFirst()
                    .get();
        } catch (NoSuchElementException e) {
            throw new Exception("Dato non trovato");
        }

    }

    public List<EmployeeDto> findAllEmployees() {
        List<Employee> employees = employeeRepo.findAll();
        return employees.stream()
                .map(source -> modelMapper.map(source, EmployeeDto.class))
                .toList();
    }

    public Optional<EmployeeDto> findEmployeeByEmail(String email) {
        Optional<Employee> employee = employeeRepo.findEmployeeByEmail(email);
        return employee.stream()
                .map(source -> modelMapper.map(source, EmployeeDto.class))
                .findFirst();
    }

    public Optional<EmployeeDto> findEmployeeById(Long id) {
        Optional<Employee> employee = employeeRepo.findEmployeeById(id);
        return employee.stream()
                .map(source -> modelMapper.map(source, EmployeeDto.class))
                .findFirst();
    }

    public void deleteEmployee(Long id) {
        employeeRepo.deleteEmployeeById(id);
    }

    private Employee saveChanges(EmployeeDto employeeDto) {
        Employee employee = new Employee();
        employee.setEmail(employeeDto.getEmail());
        employee.setName(employeeDto.getName());
        employee.setPassword(employeeDto.getPassword());
        return employee;
    }

    private Employee saveChanges(EmployeeDto employeeDto, Employee employee) throws Exception {
        if (!employeeDto.getEmail().equals(employee.getEmail())) {
            if (employeeRepo.findEmployeeByEmail(employee.getEmail()).isPresent()) {
                throw new Exception("Nuovo nome utente non disponibile, ritentare");
            }
            employee.setEmail(employeeDto.getEmail());
        }
        employee.setName(employeeDto.getName());
        employee.setPassword(employeeDto.getPassword());
        return employee;
    }
}
