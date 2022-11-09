package com.project.webapp.estimatemanager.controller;

import com.project.webapp.estimatemanager.dtos.EstimateDto;
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

    @Autowired
    public EstimateController(EstimateService estimateService) {
        this.estimateService = estimateService;
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

    @PostMapping(value = "/add")
    public ResponseEntity<EstimateDto> addEstimate(@RequestBody EstimateDto estimateDto) {
        if (estimateService.findEstimateById(estimateDto.getId()).isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        estimateService.addEstimate(estimateDto);
        return new ResponseEntity<>(estimateDto, HttpStatus.CREATED);
    }

    //@PutMapping(value = "/update")
    //public ResponseEntity<Estimate> updateEstimate(@RequestBody Estimate estimate) {
        //if (estimateService.findEstimateById(estimate.getId()).isPresent()) {
            //Estimate dbEstimate = estimateService.findEstimateById(estimate.getId()).get();
            //if (dbEstimate.getEmployee().equals(estimate.getEmployee()) && dbEstimate.getClient().equals(estimate.getClient()) && dbEstimate.getProduct().equals(estimate.getProduct())) {
                //Estimate updateEstimate = estimateService.updateEstimate(estimate);
                //return new ResponseEntity<>(estimate, HttpStatus.OK);
            //} else {
                //return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            //}
        //}
        //return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    //}

    //@DeleteMapping(value = "/delete")
    //public ResponseEntity<?> deleteEstimate(@RequestParam(name = "id") Long id) {
        //if (estimateService.findEstimateById(id).isPresent()) {
            //estimateService.deleteEstimate(id);
            //return new ResponseEntity<>(HttpStatus.OK);
        //}
        //return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    //}
}
