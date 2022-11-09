package com.project.webapp.estimatemanager.controller;

import com.project.webapp.estimatemanager.dtos.EmployeeDto;
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
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        List<EmployeeDto> employees = employeeService.findAllEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping(value = "/find")
    public ResponseEntity<EmployeeDto> getEmployeeByEmail(@RequestParam("email") String email) {
        if (employeeService.findEmployeeByEmail(email).isPresent()) {
            EmployeeDto employee = employeeService.findEmployeeByEmail(email).get();
            return new ResponseEntity<>(employee, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/add")
    public ResponseEntity<EmployeeDto> addEmployee(@RequestBody EmployeeDto employeeDto) {
        if (employeeService.findEmployeeByEmail(employeeDto.getEmail()).isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        employeeService.addEmployee(employeeDto);
        return new ResponseEntity<>(employeeDto, HttpStatus.CREATED);
    }

    //@PutMapping(value = "/update")
    //public ResponseEntity<Employee> updateEmployee(@RequestBody Employee employee) {
        //if (employeeService.findEmployeeByEmail(employee.getEmail()).isPresent()) {
            //Employee updateEmployee = employeeService.updateEmployee(employee);
            //return new ResponseEntity<>(updateEmployee, HttpStatus.OK);
        //}
        //return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    //}

    //@DeleteMapping(value = "/delete")
    //public ResponseEntity<?> deleteEmployee(@RequestParam("email") String email) {
        //if (employeeService.findEmployeeByEmail(email).isPresent()) {
            //employeeService.deleteEmployee(email);
            //return new ResponseEntity<>(HttpStatus.OK);
        //}
        //return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    //}
}
