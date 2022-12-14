package com.project.webapp.estimatemanager.service;

import com.project.webapp.estimatemanager.dtos.EstimateDto;
import com.project.webapp.estimatemanager.dtos.OptDto;
import com.project.webapp.estimatemanager.exception.GenericException;
import com.project.webapp.estimatemanager.models.Estimate;
import com.project.webapp.estimatemanager.models.Opt;
import com.project.webapp.estimatemanager.models.Role;
import com.project.webapp.estimatemanager.models.UserEntity;
import com.project.webapp.estimatemanager.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class EstimateService {
    private final EstimateRepo estimateRepo;
    private final UserRepo userRepo;
    private final OptionRepo optionRepo;
    private final ProductRepo productRepo;
    private final ModelMapper modelMapper;
    private final Role role;

    @Autowired
    public EstimateService(EstimateRepo estimateRepo, UserRepo userRepo, OptionRepo optionRepo, ProductRepo productRepo, ModelMapper modelMapper) {
        this.estimateRepo = estimateRepo;
        this.userRepo = userRepo;
        this.optionRepo = optionRepo;
        this.productRepo = productRepo;
        this.modelMapper = modelMapper;
        role = new Role();
        role.setName("CLIENT");
    }

    public EstimateDto addEstimate(EstimateDto estimateDto) throws Exception {
        try {
            Estimate estimate = this.saveChanges(estimateDto);
            Optional<Estimate> savedEstimate = Optional.of(estimateRepo.save(estimate));
            return savedEstimate
                    .stream()
                    .map(source -> modelMapper.map(source, EstimateDto.class))
                    .findFirst()
                    .orElseThrow();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Elemento non trovato");
        } catch (NullPointerException e) {
            throw new NoSuchElementException("Nessun elemento nella lista");
        } catch (GenericException e) {
            throw new Exception(e.getMessage());
        } catch (Exception e) {
            throw new Exception("Problema sconosciuto");
        }
    }

    public EstimateDto updateEstimate(EstimateDto estimateDto) throws Exception {
        try {
            Estimate estimate = estimateRepo.findEstimateById(estimateDto.getId()).orElseThrow();
            Estimate modifiedEstimate = this.saveChanges(estimateDto, estimate);
            estimateRepo.save(modifiedEstimate);
            return estimateRepo.findEstimateById(modifiedEstimate.getId()).stream()
                    .map(source -> modelMapper.map(source, EstimateDto.class))
                    .findFirst()
                    .orElseThrow();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Elemento non trovato");
        } catch (NullPointerException e) {
            throw new NoSuchElementException("Nessun elemento nella lista");
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public List<EstimateDto> findAllEstimates() throws Exception {
        try {
            List<Estimate> estimates = estimateRepo.findAll();
            return estimates.stream()
                    .map(source -> modelMapper.map(source, EstimateDto.class))
                    .toList();
        } catch (Exception e) {
            throw new Exception("Problema sconosciuto");
        }
    }

    public EstimateDto findEstimateById(Long id) throws Exception {
        try {
            Optional<Estimate> estimate = estimateRepo.findEstimateById(id);
            if (estimate.isPresent())
                return estimate
                        .stream()
                        .map(source -> modelMapper.map(source, EstimateDto.class))
                        .findFirst()
                        .get();
            else
                throw new NoSuchElementException("Nessun elemento trovato");
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new Exception("Problema sconosciuto");
        }
    }

    public List<EstimateDto> findEstimatesByUserId(Long id) throws Exception {
        try {
            if (userRepo.findUserEntityById(id).isPresent()) {
                UserEntity user = userRepo.findUserEntityById(id).get();
                if (user.getRoles().stream().map(Role::getName).toList().contains(role.getName()))
                    return estimateRepo
                            .findEstimatesByClient(user)
                            .stream()
                            .map(source -> modelMapper.map(source, EstimateDto.class))
                            .toList();
                else
                    return estimateRepo
                            .findEstimatesByEmployee(user)
                            .stream()
                            .map(source -> modelMapper.map(source, EstimateDto.class))
                            .toList();
            } else
                throw new NoSuchElementException("Nessun utente trovato");
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new Exception("Problema sconosciuto");
        }
    }

    public void deleteEstimate(Long id) throws Exception {
        try {
            if (estimateRepo.findEstimateById(id).isPresent())
                estimateRepo.deleteEstimateById(id);
            else
                throw new NoSuchElementException("Nessun elemento trovato");
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Elemento non trovato");
        } catch (Exception e) {
            throw new Exception("Problema sconosciuto");
        }
    }

    private Estimate saveChanges(EstimateDto estimateDto) throws Exception {
        try {
            Estimate estimate = new Estimate();
            estimate.setClient(userRepo.findUserEntityById(estimateDto.getClient().getId()).orElseThrow());
            estimate.setEmployee(userRepo.findUserEntityByEmail("default").orElseThrow());
            estimate.setProduct(productRepo.findProductById(estimateDto.getProduct().getId()).orElseThrow());
            estimate.setOptions(this.modifyOptions(estimateDto.getOptions()));
            estimate.setPrice(estimateDto.getPrice());
            return estimate;
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException();
        } catch (GenericException e) {
            throw new Exception(e.getMessage());
        } catch (Exception e) {
            throw new GenericException("Qualcosa è andato storto durante il salvataggio");
        }
    }

    private Estimate saveChanges(EstimateDto estimateDto, Estimate estimate) throws Exception {
        try {
            if (!estimateDto.getPrice().equals(estimate.getPrice())) {
                if (estimateDto.getEmployee().getId().equals(estimate.getEmployee().getId()) || estimate.getEmployee().getName().equals("default")) {
                    estimate.setPrice(estimateDto.getPrice());
                    estimate.setEmployee(userRepo.findUserEntityById(estimateDto.getEmployee().getId()).orElseThrow());
                } else {
                    throw new GenericException("Tentativo modifica informazioni di base del preventivo (impiegato)");
                }
            } else if (estimateDto.getProduct().getId().equals(estimate.getProduct().getId()) &&
                    estimateDto.getClient().getId().equals(estimate.getClient().getId()) &&
                    estimateDto.getEmployee().getId().equals(estimate.getEmployee().getId())
            ) {
                estimate.setOptions(this.modifyOptions(estimateDto.getOptions()));
            } else {
                throw new GenericException("Tentativo modifica informazioni di base del preventivo (cliente)");
            }
            return estimate;
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException();
        } catch (GenericException e) {
            throw new Exception(e.getMessage());
        } catch (Exception e) {
            throw new Exception("Problema sconosciuto");
        }
    }

    private Set<Opt> modifyOptions(Set<OptDto> optionDto) throws GenericException {
        try {
            Set<Opt> opts = new HashSet<>();
            for (OptDto opt: optionDto) {
                if (optionRepo.findOptById(opt.getId()).isPresent())
                    opts.add(optionRepo.findOptById(opt.getId()).get());
                else
                    throw new GenericException("Opzione non trovata");
            }
            return opts;
        } catch (GenericException e) {
            throw new GenericException(e.getMessage());
        } catch (Exception e) {
            throw new GenericException("Problema sconosciuto");
        }
    }
}