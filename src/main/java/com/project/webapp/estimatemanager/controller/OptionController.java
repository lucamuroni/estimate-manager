package com.project.webapp.estimatemanager.controller;

import com.project.webapp.estimatemanager.dtos.OptDto;
import com.project.webapp.estimatemanager.exception.GenericException;
import com.project.webapp.estimatemanager.exception.NameAlreadyTakenException;
import com.project.webapp.estimatemanager.service.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(value = "/option")
public class OptionController {
    private final OptionService optionsService;

    @Autowired
    public OptionController(OptionService optionsService) {
        this.optionsService = optionsService;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<OptDto>> getAllOptions() throws GenericException {
        try {
            List<OptDto> options = optionsService.findAllOptions();
            return new ResponseEntity<>(options, HttpStatus.OK);
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }

    @GetMapping(value = "/find/{id}")
    public ResponseEntity<OptDto> getOptionById(@PathVariable("id") Long id) throws NoSuchElementException, GenericException {
        try {
            OptDto option = optionsService.findOptionById(id);
            return new ResponseEntity<>(option, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }

    @PostMapping(value = "/add")
    public ResponseEntity<OptDto> addOption(@RequestBody OptDto optionDto) throws NameAlreadyTakenException, NoSuchElementException, GenericException {
        try {
            OptDto newOption = optionsService.addOption(optionDto);
            return new ResponseEntity<>(newOption, HttpStatus.CREATED);
        } catch (NameAlreadyTakenException e) {
            throw new NameAlreadyTakenException(e.getMessage());
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }

    @PutMapping(value = "/update")
    public ResponseEntity<OptDto> updateOption(@RequestBody OptDto optionDto) throws NoSuchElementException, NameAlreadyTakenException, GenericException {
        try {
            OptDto updateOption = optionsService.updateOption(optionDto);
            return new ResponseEntity<>(updateOption, HttpStatus.OK);
        } catch (NameAlreadyTakenException e) {
            throw new NameAlreadyTakenException(e.getMessage());
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteOption(@PathVariable("id") Long id) throws NoSuchElementException, GenericException {
        try {
            optionsService.deleteOption(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }
}
