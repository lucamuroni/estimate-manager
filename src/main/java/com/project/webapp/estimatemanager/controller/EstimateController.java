package com.project.webapp.estimatemanager.controller;

import com.project.webapp.estimatemanager.dtos.EstimateDto;
import com.project.webapp.estimatemanager.service.ClientService;
import com.project.webapp.estimatemanager.service.EmployeeService;
import com.project.webapp.estimatemanager.service.EstimateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/estimate")
public class EstimateController {
    private final EstimateService estimateService;
    private final ClientService clientService;
    private final EmployeeService employeeService;

    @Autowired
    public EstimateController(EstimateService estimateService, ClientService clientService, EmployeeService employeeService) {
        this.estimateService = estimateService;
        this.clientService = clientService;
        this.employeeService = employeeService;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<EstimateDto>> getAllEstimates() {
        List<EstimateDto> estimates = estimateService.findAllEstimates();
        return new ResponseEntity<>(estimates, HttpStatus.OK);
    }

    @GetMapping(value = "/find")
    public ResponseEntity<EstimateDto> getEstimateById(@RequestParam(name = "id") Long id) {
        if (estimateService.findEstimateById(id).isPresent()) {
            EstimateDto estimate = estimateService.findEstimateById(id).get();
            return new ResponseEntity<>(estimate, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/client")
    public ResponseEntity<List<EstimateDto>> getEstimatesByClientId(@RequestParam(name = "id") Long id) {
        if (clientService.findClientById(id).isPresent()) {
            List<EstimateDto> estimates = estimateService.findEstimatesByClientId(id);
            return new ResponseEntity<>(estimates, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/employee")
    public ResponseEntity<List<EstimateDto>> getEstimatesByEmployeeId(@RequestParam(name = "id") Long id) {
        if (employeeService.findEmployeeById(id).isPresent()) {
            List<EstimateDto> estimates = estimateService.findEstimateByEmployeeId(id);
            return new ResponseEntity<>(estimates, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/unmanaged")
    public ResponseEntity<List<EstimateDto>> getEstimatesUnmanaged() {
        List<EstimateDto> estimates = estimateService.findEstimateByEmployeeId(employeeService.findEmployeeByEmail("default").get().getId());
        if (estimates.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(estimates, HttpStatus.OK);
    }

    @PostMapping(value = "/add")
    public ResponseEntity<EstimateDto> addEstimate(@RequestBody EstimateDto estimateDto) {
        if (estimateService.findEstimateById(estimateDto.getId()).isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        EstimateDto newEstimate = estimateService.addEstimate(estimateDto);
        return new ResponseEntity<>(newEstimate, HttpStatus.CREATED);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<EstimateDto> updateEstimate(@RequestBody EstimateDto estimateDto) {
        if (estimateService.findEstimateById(estimateDto.getId()).isPresent()) {
            EstimateDto updateEstimate = estimateService.updateEstimate(estimateDto);
            return new ResponseEntity<>(updateEstimate, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<?> deleteEstimate(@RequestParam(name = "id") Long id) {
        if (estimateService.findEstimateById(id).isPresent()) {
            estimateService.deleteEstimate(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
