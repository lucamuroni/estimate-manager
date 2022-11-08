package com.project.webapp.estimatemanager.controller;

import com.project.webapp.estimatemanager.models.Employee;
import com.project.webapp.estimatemanager.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//TODO: inserire tutti i try catch
@RestController
@RequestMapping(value = "/employee")
public class EmployeeController {
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.findAllEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping(value = "/find")
    public ResponseEntity<Employee> getEmployeeByEmail(@RequestParam("email") String email) {
        if (employeeService.findEmployeeByEmail(email).isPresent()) {
            Employee employee = employeeService.findEmployeeByEmail(email).get();
            return new ResponseEntity<>(employee, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/add")
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee) {
        if (employeeService.findEmployeeByEmail(employee.getEmail()).isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        Employee newEmployee = employeeService.addEmployee(employee);
        return new ResponseEntity<>(newEmployee, HttpStatus.CREATED);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<Employee> updateEmployee(@RequestBody Employee employee) {
        if (employeeService.findEmployeeByEmail(employee.getEmail()).isPresent()) {
            Employee updateEmployee = employeeService.updateEmployee(employee);
            return new ResponseEntity<>(updateEmployee, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<?> deleteEmployee(@RequestParam("email") String email) {
        if (employeeService.findEmployeeByEmail(email).isPresent()) {
            employeeService.deleteEmployee(email);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
