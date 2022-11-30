package com.project.webapp.estimatemanager.service;

import com.project.webapp.estimatemanager.dtos.EstimateDto;
import com.project.webapp.estimatemanager.dtos.OptDto;
import com.project.webapp.estimatemanager.exception.GenericException;
import com.project.webapp.estimatemanager.models.Estimate;
import com.project.webapp.estimatemanager.models.Opt;
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
    private final ClientRepo clientRepo;
    private final EmployeeRepo employeeRepo;
    private final ProductRepo productRepo;
    private final OptionRepo optionRepo;
    private final ModelMapper modelMapper;

    @Autowired
    public EstimateService(EstimateRepo estimateRepo, ClientRepo clientRepo, EmployeeRepo employeeRepo, ProductRepo productRepo, OptionRepo optionRepo, ModelMapper modelMapper) {
        this.estimateRepo = estimateRepo;
        this.clientRepo = clientRepo;
        this.employeeRepo = employeeRepo;
        this.productRepo = productRepo;
        this.optionRepo = optionRepo;
        this.modelMapper = modelMapper;
    }

    public EstimateDto addEstimate(EstimateDto estimateDto) throws Exception {
        try {
            Estimate estimate = this.saveChanges(estimateDto);
            estimateRepo.save(estimate);
            return estimateRepo.findEstimateById(estimate.getId()).stream()
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

    public Optional<EstimateDto> findEstimateById(Long id) throws Exception {
        try {
            Optional<Estimate> estimate = estimateRepo.findEstimateById(id);
            return estimate.stream()
                    .map(source -> modelMapper.map(source, EstimateDto.class))
                    .findFirst();
        } catch (NullPointerException e) {
            throw new Exception("Nessun elemento nella lista");
        } catch (Exception e) {
            throw new Exception("Problema sconosciuto");
        }
    }

    public List<EstimateDto> findEstimatesByClientId(Long id) throws Exception {
        try {
            List<Estimate> estimates = estimateRepo.findEstimatesByClient(clientRepo.findClientById(id).orElseThrow());
            return estimates.stream()
                    .map(source -> modelMapper.map(source, EstimateDto.class))
                    .toList();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Elemento non trovato");
        } catch (Exception e) {
            throw new Exception("Problema sconosciuto");
        }
    }

    public List<EstimateDto> findEstimateByEmployeeId(Long id) throws Exception {
        try {
            List<Estimate> estimates = estimateRepo.findEstimatesByEmployee(employeeRepo.findEmployeeById(id).orElseThrow());
            return estimates.stream()
                    .map(source -> modelMapper.map(source, EstimateDto.class))
                    .toList();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Elemento non trovato");
        } catch (Exception e) {
            throw new Exception("Problema sconosciuto");
        }
    }

    public void deleteEstimate(Long id) throws Exception {
        try {
            estimateRepo.deleteEstimateById(id);
        } catch (Exception e) {
            throw new Exception("Problema sconosciuto");
        }
    }

    private Estimate saveChanges(EstimateDto estimateDto) throws Exception {
        try {
            Estimate estimate = new Estimate();
            estimate.setClient(clientRepo.findClientById(estimateDto.getClient().getId()).orElseThrow());
            estimate.setEmployee(employeeRepo.findEmployeeByEmail("default").orElseThrow());
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
                    estimate.setEmployee(employeeRepo.findEmployeeById(estimateDto.getEmployee().getId()).orElseThrow());
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
