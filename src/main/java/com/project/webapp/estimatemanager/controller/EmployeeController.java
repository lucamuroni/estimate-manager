package com.project.webapp.estimatemanager.controller;

import com.project.webapp.estimatemanager.dtos.EmployeeDto;
import com.project.webapp.estimatemanager.exception.UserNotFoundException;
import com.project.webapp.estimatemanager.exception.NameAlreadyTakenException;
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
    public ResponseEntity<EmployeeDto> getEmployeeByEmail(@RequestParam("id") Long id) throws UserNotFoundException {
        if (employeeService.findEmployeeById(id).isEmpty()) {
            throw new UserNotFoundException("Impiegato assente o id errato");
        }
        EmployeeDto employee = employeeService.findEmployeeById(id).get();
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @PostMapping(value = "/add")
    public ResponseEntity<EmployeeDto> addEmployee(@RequestBody EmployeeDto employeeDto) throws NameAlreadyTakenException {
        if (employeeService.findEmployeeByEmail(employeeDto.getEmail()).isPresent())
            throw new NameAlreadyTakenException("Nome utente non disponibile");
        EmployeeDto newEmployee = employeeService.addEmployee(employeeDto);
        return new ResponseEntity<>(newEmployee, HttpStatus.CREATED);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<EmployeeDto> updateEmployee(@RequestBody EmployeeDto employeeDto) throws UserNotFoundException, NameAlreadyTakenException {
        if (employeeService.findEmployeeById(employeeDto.getId()).isEmpty()) {
            throw new UserNotFoundException("Impiegato assente o id errato");
        }
        EmployeeDto updateEmployee;
        try {
            updateEmployee = employeeService.updateEmployee(employeeDto);
        } catch (Exception e) {
            throw new NameAlreadyTakenException(e.getMessage());
        }
        return new ResponseEntity<>(updateEmployee, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<?> deleteEmployee(@RequestParam("id") Long id) throws UserNotFoundException {
        if (employeeService.findEmployeeById(id).isEmpty()) {
            throw new UserNotFoundException("Impiegato assente o id errato");
        }
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
