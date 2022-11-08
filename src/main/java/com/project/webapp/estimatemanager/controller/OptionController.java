package com.project.webapp.estimatemanager.controller;

import com.project.webapp.estimatemanager.models.Opt;
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
    public ResponseEntity<List<Opt>> getAllOptions() {
        List<Opt> options = optionsService.findAllOptions();
        return new ResponseEntity<>(options, HttpStatus.OK);
    }

    @GetMapping(value = "/find")
    public ResponseEntity<Opt> getOptionById(@RequestParam(name = "id") Long id) {
        if (optionsService.findOptionById(id).isPresent()) {
            Opt option = optionsService.findOptionById(id).get();
            return new ResponseEntity<>(option, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/add")
    public ResponseEntity<Opt> addOption(@RequestBody Opt option) {
        if (optionsService.findOptionByName(option.getName()).isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        Opt newOption = optionsService.addOption(option);
        return new ResponseEntity<>(newOption, HttpStatus.CREATED);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<Opt> updateOption(@RequestBody Opt option) {
        if (optionsService.findOptionById(option.getId()).isPresent()) {
            Opt updateOption = optionsService.updateOption(option);
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
