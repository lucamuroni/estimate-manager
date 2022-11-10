package com.project.webapp.estimatemanager.controller;

import com.project.webapp.estimatemanager.dtos.EstimateDto;
import com.project.webapp.estimatemanager.exception.AccessNotAllowedException;
import com.project.webapp.estimatemanager.exception.DataNotFoundException;
import com.project.webapp.estimatemanager.exception.EstimateNotFoundException;
import com.project.webapp.estimatemanager.exception.UserNotFoundException;
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
    public ResponseEntity<EstimateDto> getEstimateById(@RequestParam(name = "id") Long id) throws EstimateNotFoundException {
        if (estimateService.findEstimateById(id).isEmpty()) {
            throw new EstimateNotFoundException("Preventivo assente o id errato");
        }
        EstimateDto estimate = estimateService.findEstimateById(id).get();
        return new ResponseEntity<>(estimate, HttpStatus.OK);
    }

    @GetMapping(value = "/client")
    public ResponseEntity<List<EstimateDto>> getEstimatesByClientId(@RequestParam(name = "id") Long id) throws UserNotFoundException, DataNotFoundException {
        if (clientService.findClientById(id).isEmpty()) {
            throw new UserNotFoundException("Cliente non trovato o id errato, preventivi non disponibili");
        }
        try {
            List<EstimateDto> estimates = estimateService.findEstimatesByClientId(id);
            return new ResponseEntity<>(estimates, HttpStatus.OK);
        } catch (Exception e) {
            throw new DataNotFoundException(e.getMessage());
        }

    }

    @GetMapping(value = "/employee")
    public ResponseEntity<List<EstimateDto>> getEstimatesByEmployeeId(@RequestParam(name = "id") Long id) throws UserNotFoundException, DataNotFoundException {
        if (employeeService.findEmployeeById(id).isEmpty()) {
            throw new UserNotFoundException("Impiegato non trovato o id errato, preventivi non disponibili");
        }
        try {
            List<EstimateDto> estimates = estimateService.findEstimateByEmployeeId(id);
            return new ResponseEntity<>(estimates, HttpStatus.OK);
        } catch (Exception e) {
            throw new DataNotFoundException(e.getMessage());
        }

    }

    @GetMapping(value = "/unmanaged")
    public ResponseEntity<List<EstimateDto>> getEstimatesUnmanaged() throws DataNotFoundException {
        try {
            List<EstimateDto> estimates = estimateService.findEstimateByEmployeeId(employeeService.findEmployeeByEmail("default").get().getId());
            if (estimates.isEmpty())
                throw new DataNotFoundException("Nessun preventivo risulta ancora dover essere gestito da un impiegato");
            return new ResponseEntity<>(estimates, HttpStatus.OK);
        } catch (Exception e) {
            throw new DataNotFoundException("Dato non trovato");
        }

    }

    //Gestione eccezioni mancante
    @PostMapping(value = "/add")
    public ResponseEntity<EstimateDto> addEstimate(@RequestBody EstimateDto estimateDto) throws DataNotFoundException {
        EstimateDto newEstimate;
        try {
            newEstimate = estimateService.addEstimate(estimateDto);
        } catch (Exception e) {
            throw new DataNotFoundException(e.getMessage());
        }
        return new ResponseEntity<>(newEstimate, HttpStatus.CREATED);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<EstimateDto> updateEstimate(@RequestBody EstimateDto estimateDto) throws EstimateNotFoundException, AccessNotAllowedException {
        if (estimateService.findEstimateById(estimateDto.getId()).isEmpty()) {
            throw new EstimateNotFoundException("Preventivo assente o id errato");
        }
        EstimateDto updateEstimate;
        try {
            updateEstimate = estimateService.updateEstimate(estimateDto);
        } catch (Exception e) {
            throw new AccessNotAllowedException(e.getMessage());
        }
        return new ResponseEntity<>(updateEstimate, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<?> deleteEstimate(@RequestParam(name = "id") Long id) throws EstimateNotFoundException {
        if (estimateService.findEstimateById(id).isEmpty()) {
            throw new EstimateNotFoundException("Preventivo assente o id errato");
        }
        estimateService.deleteEstimate(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
