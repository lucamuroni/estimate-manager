package com.project.webapp.estimatemanager.service;

import com.project.webapp.estimatemanager.dtos.OptDto;
import com.project.webapp.estimatemanager.models.Opt;
import com.project.webapp.estimatemanager.repository.OptionRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
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

    public Opt addOption(OptDto optionDto) {
        Opt option = new Opt();
        option.setName(optionDto.getName());
        option.setType(optionDto.getType());
        return optionRepo.save(option);
    }

    public List<OptDto> findAllOptions() {
        List<Opt> options = optionRepo.findAll();
        return options.stream()
                .map(source -> modelMapper.map(source, OptDto.class))
                .toList();
    }

    public Opt updateOption(Opt option) {
        return optionRepo.save(option);
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
}
