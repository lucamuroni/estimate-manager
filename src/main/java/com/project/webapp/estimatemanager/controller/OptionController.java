package com.project.webapp.estimatemanager.controller;

import com.project.webapp.estimatemanager.dtos.OptDto;
import com.project.webapp.estimatemanager.exception.NameAlreadyTakenException;
import com.project.webapp.estimatemanager.exception.OptionNotFoundException;
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
    public ResponseEntity<OptDto> getOptionById(@RequestParam(name = "id") Long id) throws OptionNotFoundException {
        if (optionsService.findOptionById(id).isEmpty()) {
            throw new OptionNotFoundException("Opzione assente o id errato");
        }
        OptDto option = optionsService.findOptionById(id).get();
        return new ResponseEntity<>(option, HttpStatus.OK);
    }

    @PostMapping(value = "/add")
    public ResponseEntity<OptDto> addOption(@RequestBody OptDto optionDto) throws NameAlreadyTakenException {
        if (optionsService.findOptionByName(optionDto.getName()).isPresent())
            throw new NameAlreadyTakenException("Nome opzione non disponibile");
        OptDto newOption = optionsService.addOption(optionDto);
        return new ResponseEntity<>(newOption, HttpStatus.CREATED);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<OptDto> updateOption(@RequestBody OptDto optionDto) throws OptionNotFoundException, NameAlreadyTakenException {
        if (optionsService.findOptionById(optionDto.getId()).isEmpty()) {
            throw new OptionNotFoundException("Opzione assente o id errato");
        }
        OptDto updateOption;
        try {
            updateOption = optionsService.updateOption(optionDto);
        } catch (Exception e) {
            throw new NameAlreadyTakenException(e.getMessage());
        }
        return new ResponseEntity<>(updateOption, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<?> deleteOption(@RequestParam(name = "id") Long id) throws OptionNotFoundException {
        if (optionsService.findOptionById(id).isEmpty()) {
            throw new OptionNotFoundException("Opzione assente o id errato");
        }
        optionsService.deleteOption(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
