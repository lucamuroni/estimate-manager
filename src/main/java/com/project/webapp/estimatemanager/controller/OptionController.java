package com.project.webapp.estimatemanager.controller;

import com.project.webapp.estimatemanager.dtos.OptDto;
import com.project.webapp.estimatemanager.service.OptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//TODO: inserire tutti i try catch
//TODO: crere metodi di collegamento di un'opzione ad un prodotto
@RestController
@RequestMapping(value = "/option")
public class OptionController {
    private final OptionService optionsService;

    //@Autowired
    public OptionController(OptionService optionsService) {
        this.optionsService = optionsService;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<OptDto>> getAllOptions() {
        List<OptDto> options = optionsService.findAllOptions();
        return new ResponseEntity<>(options, HttpStatus.OK);
    }

    @GetMapping(value = "/find")
    public ResponseEntity<OptDto> getOptionById(@RequestParam(name = "id") Long id) {
        if (optionsService.findOptionById(id).isPresent()) {
            OptDto option = optionsService.findOptionById(id).get();
            return new ResponseEntity<>(option, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/add")
    public ResponseEntity<OptDto> addOption(@RequestBody OptDto optionDto) {
        if (optionsService.findOptionByName(optionDto.getName()).isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        OptDto newOption = optionsService.addOption(optionDto);
        return new ResponseEntity<>(newOption, HttpStatus.CREATED);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<OptDto> updateOption(@RequestBody OptDto optionDto) {
        if (optionsService.findOptionById(optionDto.getId()).isPresent()) {
            OptDto updateOption = optionsService.updateOption(optionDto);
            return new ResponseEntity<>(updateOption, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<?> deleteOption(@RequestParam(name = "id") Long id) {
        if (optionsService.findOptionById(id).isPresent()) {
            optionsService.deleteOption(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
