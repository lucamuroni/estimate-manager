package com.project.webapp.estimatemanager.service;

import com.project.webapp.estimatemanager.dtos.EmployeeDto;
import com.project.webapp.estimatemanager.exception.GenericException;
import com.project.webapp.estimatemanager.exception.NameAlreadyTakenException;
import com.project.webapp.estimatemanager.models.Employee;
import com.project.webapp.estimatemanager.repository.EmployeeRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

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

    public EmployeeDto addEmployee(EmployeeDto employeeDto) throws Exception {
        try {
            Employee employee = this.saveChanges(employeeDto);
            employeeRepo.save(employee);
            return employeeRepo.findEmployeeByEmail(employee.getEmail()).stream()
                    .map(source -> modelMapper.map(source, EmployeeDto.class))
                    .findFirst()
                    .get();
        } catch (Exception e) {
            throw new Exception("Problema sconosciuto");
        }
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
        } catch (NameAlreadyTakenException e) {
            throw new NameAlreadyTakenException(e.getMessage());
        } catch (GenericException e) {
            throw new GenericException(e.getMessage());
        } catch (Exception e) {
            throw new Exception("Problema sconosciuto");
        }
    }

    public List<EmployeeDto> findAllEmployees() throws Exception {
        try {
            List<Employee> employees = employeeRepo.findAll();
            return employees.stream()
                    .map(source -> modelMapper.map(source, EmployeeDto.class))
                    .toList();
        } catch (Exception e) {
            throw new Exception("Problema sconosciuto");
        }
    }

    public Optional<EmployeeDto> findEmployeeByEmail(String email) throws Exception {
        try {
            Optional<Employee> employee = employeeRepo.findEmployeeByEmail(email);
            return employee.stream()
                    .map(source -> modelMapper.map(source, EmployeeDto.class))
                    .findFirst();
        } catch (Exception e) {
            throw new Exception("Problema sconosciuto");
        }
    }

    public Optional<EmployeeDto> findEmployeeById(Long id) throws Exception {
        try {
            Optional<Employee> employee = employeeRepo.findEmployeeById(id);
            return employee.stream()
                    .map(source -> modelMapper.map(source, EmployeeDto.class))
                    .findFirst();
        } catch (Exception e) {
            throw new Exception("Problema sconosciuto");
        }
    }

    public void deleteEmployee(Long id) throws Exception {
        try {
            employeeRepo.deleteEmployeeById(id);
        } catch (Exception e) {
            throw new Exception("Problema sconosciuto");
        }
    }

    private Employee saveChanges(EmployeeDto employeeDto) {
        Employee employee = new Employee();
        employee.setEmail(employeeDto.getEmail());
        employee.setName(employeeDto.getName());
        employee.setPassword(employeeDto.getPassword());
        return employee;
    }

    private Employee saveChanges(EmployeeDto employeeDto, Employee employee) throws NameAlreadyTakenException, GenericException {
        try {
            if (!employeeDto.getEmail().equals(employee.getEmail())) {
                if (employeeRepo.findEmployeeByEmail(employee.getEmail()).isPresent()) {
                    throw new NameAlreadyTakenException("Nuovo nome utente non disponibile, ritentare");
                }
                employee.setEmail(employeeDto.getEmail());
            }
            employee.setName(employeeDto.getName());
            employee.setPassword(employeeDto.getPassword());
            return employee;
        } catch (NameAlreadyTakenException e) {
            throw new NameAlreadyTakenException(e.getMessage());
        } catch (Exception e) {
            throw new GenericException("Problema sconosciuto");
        }
    }
}
