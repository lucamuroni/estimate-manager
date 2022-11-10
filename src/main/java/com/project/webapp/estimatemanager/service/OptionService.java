package com.project.webapp.estimatemanager.service;

import com.project.webapp.estimatemanager.dtos.OptDto;
import com.project.webapp.estimatemanager.models.Opt;
import com.project.webapp.estimatemanager.repository.OptionRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

//TODO: inserire tutti i try catch
@Service
@Transactional
public class OptionService {
    private final OptionRepo optionRepo;
    private final ModelMapper modelMapper;

    @Autowired
    public OptionService(OptionRepo optionRepo, ModelMapper modelMapper) {
        this.optionRepo = optionRepo;
        this.modelMapper = modelMapper;
    }

    public OptDto addOption(OptDto optionDto) {
        Opt option = new Opt();
        option.setName(optionDto.getName());
        option.setType(optionDto.getType());
        optionRepo.save(option);
        return optionRepo.findOptById(option.getId()).stream()
                .map(source -> modelMapper.map(source, OptDto.class))
                .findFirst()
                .get();
    }

    public OptDto updateOption(OptDto optionDto) throws Exception {
        try {
            Opt option = optionRepo.findOptById(optionDto.getId()).get();
            Opt modifiedOption = this.saveChanges(optionDto, option);
            optionRepo.save(modifiedOption);
            return optionRepo.findOptById(modifiedOption.getId()).stream()
                    .map(source -> modelMapper.map(source, OptDto.class))
                    .findFirst()
                    .get();
        } catch (NoSuchElementException e) {
            throw new Exception("Dato non trovato");
        }

    }

    public List<OptDto> findAllOptions() {
        List<Opt> options = optionRepo.findAll();
        return options.stream()
                .map(source -> modelMapper.map(source, OptDto.class))
                .toList();
    }

    public Optional<OptDto> findOptionById(Long id) {
        Optional<Opt> option = optionRepo.findOptById(id);
        return option.stream()
                .map(source -> modelMapper.map(source, OptDto.class))
                .findFirst();
    }

    public Optional<OptDto> findOptionByName(String name) {
        Optional<Opt> option = optionRepo.findOptByName(name);
        return option.stream()
                .map(source -> modelMapper.map(source, OptDto.class))
                .findFirst();
    }

    public void deleteOption(Long id) {
        optionRepo.deleteOptById(id);
    }

    private Opt saveChanges(OptDto optionDto, Opt option) throws Exception {
        if (!optionDto.getName().equals(option.getName())) {
            if (optionRepo.findOptByName(optionDto.getName()).isPresent()) {
                throw new Exception("Opzione con quel nome già esistente");
            }
            option.setName(optionDto.getName());
        }
        option.setType(optionDto.getType());
        return option;
    }
}
