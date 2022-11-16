package com.project.webapp.estimatemanager.controller;

import com.project.webapp.estimatemanager.dtos.EstimateDto;
import com.project.webapp.estimatemanager.exception.*;
import com.project.webapp.estimatemanager.service.ClientService;
import com.project.webapp.estimatemanager.service.EmployeeService;
import com.project.webapp.estimatemanager.service.EstimateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

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
    public ResponseEntity<List<EstimateDto>> getAllEstimates() throws GenericException {
        try {
            List<EstimateDto> estimates = estimateService.findAllEstimates();
            return new ResponseEntity<>(estimates, HttpStatus.OK);
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }

    @GetMapping(value = "/find")
    public ResponseEntity<EstimateDto> getEstimateById(@RequestParam(name = "id") Long id) throws EstimateNotFoundException, GenericException {
        try {
            if (estimateService.findEstimateById(id).isEmpty()) {
                throw new EstimateNotFoundException("Preventivo assente o id errato");
            }
            EstimateDto estimate = estimateService.findEstimateById(id).get();
            return new ResponseEntity<>(estimate, HttpStatus.OK);
        } catch (EstimateNotFoundException e) {
            throw new EstimateNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }

    @GetMapping(value = "/client")
    public ResponseEntity<List<EstimateDto>> getEstimatesByClientId(@RequestParam(name = "id") Long id) throws GenericException, UserNotFoundException, DataNotFoundException {
        try {
            if (clientService.findClientById(id).isEmpty()) {
                throw new UserNotFoundException("Cliente non trovato o id errato, preventivi non disponibili");
            }
            List<EstimateDto> estimates = estimateService.findEstimatesByClientId(id);
            return new ResponseEntity<>(estimates, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException(e.getMessage());
        } catch (NoSuchElementException e) {
            throw new DataNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }

    @GetMapping(value = "/employee")
    public ResponseEntity<List<EstimateDto>> getEstimatesByEmployeeId(@RequestParam(name = "id") Long id) throws UserNotFoundException, DataNotFoundException, GenericException {
        try {
            if (employeeService.findEmployeeById(id).isEmpty()) {
                throw new UserNotFoundException("Impiegato non trovato o id errato, preventivi non disponibili");
            }
            List<EstimateDto> estimates = estimateService.findEstimateByEmployeeId(id);
            return new ResponseEntity<>(estimates, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException(e.getMessage());
        } catch (NoSuchElementException e) {
            throw new DataNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }

    @GetMapping(value = "/unmanaged")
    public ResponseEntity<List<EstimateDto>> getEstimatesUnmanaged() throws DataNotFoundException, GenericException {
        try {
            List<EstimateDto> estimates = estimateService.findEstimateByEmployeeId(employeeService.findEmployeeByEmail("default").get().getId());
            if (estimates.isEmpty())
                throw new DataNotFoundException("Nessun preventivo risulta ancora dover essere gestito da un impiegato");
            return new ResponseEntity<>(estimates, HttpStatus.OK);
        } catch (DataNotFoundException e) {
            throw new DataNotFoundException("Dato non trovato");
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }

    @PostMapping(value = "/add")
    public ResponseEntity<EstimateDto> addEstimate(@RequestBody EstimateDto estimateDto) throws GenericException {
        try {
            EstimateDto newEstimate;
            newEstimate = estimateService.addEstimate(estimateDto);
            return new ResponseEntity<>(newEstimate, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }

    @PutMapping(value = "/update")
    public ResponseEntity<EstimateDto> updateEstimate(@RequestBody EstimateDto estimateDto) throws EstimateNotFoundException, GenericException {
        try {
            if (estimateService.findEstimateById(estimateDto.getId()).isEmpty()) {
                throw new EstimateNotFoundException("Preventivo assente o id errato");
            }
            EstimateDto updateEstimate;
            updateEstimate = estimateService.updateEstimate(estimateDto);
            return new ResponseEntity<>(updateEstimate, HttpStatus.OK);
        } catch (EstimateNotFoundException e) {
            throw new EstimateNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<?> deleteEstimate(@RequestParam(name = "id") Long id) throws EstimateNotFoundException, GenericException {
        try {
            if (estimateService.findEstimateById(id).isEmpty()) {
                throw new EstimateNotFoundException("Preventivo assente o id errato");
            }
            estimateService.deleteEstimate(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EstimateNotFoundException e) {
            throw new EstimateNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }
}
