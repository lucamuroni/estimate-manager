package com.project.webapp.estimatemanager.controller;

import com.project.webapp.estimatemanager.dtos.EmployeeDto;
import com.project.webapp.estimatemanager.exception.GenericException;
import com.project.webapp.estimatemanager.exception.UserNotFoundException;
import com.project.webapp.estimatemanager.exception.NameAlreadyTakenException;
import com.project.webapp.estimatemanager.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/employee")
public class EmployeeController {
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() throws GenericException {
        try {
            List<EmployeeDto> employees = employeeService.findAllEmployees();
            return new ResponseEntity<>(employees, HttpStatus.OK);
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }

    @GetMapping(value = "/find")
    public ResponseEntity<EmployeeDto> getEmployeeByEmail(@RequestParam("id") Long id) throws UserNotFoundException, GenericException {
        try {
            if (employeeService.findEmployeeById(id).isEmpty()) {
                throw new UserNotFoundException("Impiegato assente o id errato");
            }
            EmployeeDto employee = employeeService.findEmployeeById(id).get();
            return new ResponseEntity<>(employee, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }

    @PostMapping(value = "/add")
    public ResponseEntity<EmployeeDto> addEmployee(@RequestBody EmployeeDto employeeDto) throws NameAlreadyTakenException, GenericException {
        try {
            if (employeeService.findEmployeeByEmail(employeeDto.getEmail()).isPresent())
                throw new NameAlreadyTakenException("Nome utente non disponibile");
            EmployeeDto newEmployee = employeeService.addEmployee(employeeDto);
            return new ResponseEntity<>(newEmployee, HttpStatus.CREATED);
        } catch (NameAlreadyTakenException e) {
            throw new NameAlreadyTakenException(e.getMessage());
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }

    @PutMapping(value = "/update")
    public ResponseEntity<EmployeeDto> updateEmployee(@RequestBody EmployeeDto employeeDto) throws UserNotFoundException, NameAlreadyTakenException, GenericException {
        try {
            if (employeeService.findEmployeeById(employeeDto.getId()).isEmpty()) {
                throw new UserNotFoundException("Impiegato assente o id errato");
            }
            EmployeeDto updateEmployee;
            updateEmployee = employeeService.updateEmployee(employeeDto);
            return new ResponseEntity<>(updateEmployee, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException(e.getMessage());
        } catch (NameAlreadyTakenException e) {
            throw new NameAlreadyTakenException(e.getMessage());
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<?> deleteEmployee(@RequestParam("id") Long id) throws UserNotFoundException, GenericException {
        try {
            if (employeeService.findEmployeeById(id).isEmpty()) {
                throw new UserNotFoundException("Impiegato assente o id errato");
            }
            employeeService.deleteEmployee(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }
}
