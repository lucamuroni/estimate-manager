package com.project.webapp.estimatemanager.controller;

import com.project.webapp.estimatemanager.dtos.EstimateDto;
import com.project.webapp.estimatemanager.exception.*;
import com.project.webapp.estimatemanager.service.EstimateService;
import com.project.webapp.estimatemanager.service.UserService;
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
    private final UserService userService;

    @Autowired
    public EstimateController(EstimateService estimateService, UserService userService) {
        this.estimateService = estimateService;
        this.userService = userService;
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

    @GetMapping(value = "/find/{id}")
    public ResponseEntity<EstimateDto> getEstimateById(@PathVariable("id") Long id) throws NoSuchElementException, GenericException {
        try {
            EstimateDto estimate = estimateService.findEstimateById(id);
            return new ResponseEntity<>(estimate, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }

    @GetMapping(value = "/user/{id}")
    public ResponseEntity<List<EstimateDto>> getEstimatesByUserId(@PathVariable("id") Long id) throws GenericException, NoSuchElementException {
        try {
            List<EstimateDto> estimates = estimateService.findEstimatesByUserId(id);
            return new ResponseEntity<>(estimates, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }

    @GetMapping(value = "/unmanaged")
    public ResponseEntity<List<EstimateDto>> getEstimatesUnmanaged() throws NoSuchElementException, GenericException {
        try {
            List<EstimateDto> estimates = estimateService.findEstimatesByUserId(userService.findUserByEmail("default").getId());
            if (estimates.isEmpty())
                throw new NoSuchElementException("Nessun preventivo risulta ancora dover essere gestito da un impiegato");
            return new ResponseEntity<>(estimates, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Elemento non trovato");
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }

    @PostMapping(value = "/add")
    public ResponseEntity<EstimateDto> addEstimate(@RequestBody EstimateDto estimateDto) throws NoSuchElementException, GenericException {
        try {
            EstimateDto newEstimate;
            newEstimate = estimateService.addEstimate(estimateDto);
            return new ResponseEntity<>(newEstimate, HttpStatus.CREATED);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }

    @PutMapping(value = "/update")
    public ResponseEntity<EstimateDto> updateEstimate(@RequestBody EstimateDto estimateDto) throws NoSuchElementException, GenericException {
        try {
            EstimateDto updateEstimate = estimateService.updateEstimate(estimateDto);
            return new ResponseEntity<>(updateEstimate, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteEstimate(@PathVariable("id") Long id) throws NoSuchElementException, GenericException {
        try {
            estimateService.deleteEstimate(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Elemento non trovato");
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }
}
