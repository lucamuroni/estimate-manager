package com.project.webapp.estimatemanager.service;

import com.project.webapp.estimatemanager.dtos.OptDto;
import com.project.webapp.estimatemanager.exception.GenericException;
import com.project.webapp.estimatemanager.exception.NameAlreadyTakenException;
import com.project.webapp.estimatemanager.models.Opt;
import com.project.webapp.estimatemanager.repository.OptionRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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

    public OptDto addOption(OptDto optionDto) throws Exception {
        try {
            if (optionRepo.findOptByName(optionDto.getName()).isPresent())
                throw new NameAlreadyTakenException("Nome opzione non disponibile");
            else {
                Opt option = new Opt();
                option.setName(optionDto.getName());
                option.setType(optionDto.getType());
                Optional<Opt> savedOpt = Optional.of(optionRepo.save(option));
                return savedOpt
                        .stream()
                        .map(source -> modelMapper.map(source, OptDto.class))
                        .findFirst()
                        .orElseThrow();
            }
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Elemento non trovato");
        } catch (NullPointerException e) {
            throw new NoSuchElementException("Nessun elemento nella lista");
        } catch (NameAlreadyTakenException e) {
            throw new NameAlreadyTakenException(e.getMessage());
        } catch (Exception e) {
            throw new Exception("Problema sconosciuto");
        }
    }

    public OptDto updateOption(OptDto optionDto) throws Exception {
        try {
            Opt option = optionRepo.findOptById(optionDto.getId()).orElseThrow();
            Opt modifiedOption = this.saveChanges(optionDto, option);
            optionRepo.save(modifiedOption);
            return optionRepo.findOptById(modifiedOption.getId()).stream()
                    .map(source -> modelMapper.map(source, OptDto.class))
                    .findFirst()
                    .orElseThrow();
        } catch (NameAlreadyTakenException e) {
            throw new NameAlreadyTakenException(e.getMessage());
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Elemento non trovato");
        } catch (NullPointerException e) {
            throw new NoSuchElementException("Nessun elemento nella lista");
        } catch (GenericException e) {
            throw new GenericException(e.getMessage());
        } catch (Exception e) {
            throw new Exception("Problema sconosciuto");
        }
    }

    public List<OptDto> findAllOptions() throws Exception {
        try {
            List<Opt> options = optionRepo.findAll();
            return options.stream()
                    .map(source -> modelMapper.map(source, OptDto.class))
                    .toList();
        } catch (Exception e) {
            throw new Exception("Problema sconosciuto");
        }
    }

    public OptDto findOptionById(Long id) throws Exception {
        try {
            if (optionRepo.existsById(id))
                return optionRepo.findOptById(id)
                        .stream()
                        .map(source -> modelMapper.map(source, OptDto.class))
                        .findAny()
                        .get();
            else
                throw new NoSuchElementException("Nessun elemento trovato");
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new Exception("Problema sconosciuto");
        }
    }

//    public OptDto findOptionByName(String name) throws Exception {
//        try {
//            Optional<Opt> option = optionRepo.findOptByName(name);
//            if (option.isPresent())
//                return option
//                        .stream()
//                        .map(source -> modelMapper.map(source, OptDto.class))
//                        .findFirst()
//                        .orElseThrow();
//            else
//                throw new NoSuchElementException("Nessun elemento trovato");
//        } catch (NoSuchElementException e) {
//            throw new NoSuchElementException(e.getMessage());
//        } catch (Exception e) {
//            throw new Exception("Problema sconosciuto");
//        }
//    }

    public void deleteOption(Long id) throws Exception {
        try {
            if (optionRepo.findOptById(id).isPresent())
                optionRepo.deleteOptById(id);
            else
                throw new NoSuchElementException("Nessun elemento trovato");
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new Exception("Problema sconosciuto");
        }
    }

    private Opt saveChanges(OptDto optionDto, Opt option) throws NameAlreadyTakenException, GenericException {
        try {
            if (!optionDto.getName().equals(option.getName())) {
                if (optionRepo.findOptByName(optionDto.getName()).isPresent()) {
                    throw new NameAlreadyTakenException("Opzione con quel nome già esistente");
                }
                option.setName(optionDto.getName());
            }
            option.setType(optionDto.getType());
            return option;
        } catch (NameAlreadyTakenException e) {
            throw new NameAlreadyTakenException(e.getMessage());
        } catch (Exception e) {
            throw new GenericException("Problema sconosciuto");
        }
    }
}
