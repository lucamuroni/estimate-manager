package com.project.webapp.estimatemanager.service;

import com.project.webapp.estimatemanager.dtos.EstimateDto;
import com.project.webapp.estimatemanager.dtos.OptDto;
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
        Estimate estimate = this.saveChanges(estimateDto);
        estimateRepo.save(estimate);
        return estimateRepo.findEstimateById(estimate.getId()).stream()
                .map(source -> modelMapper.map(source, EstimateDto.class))
                .findFirst()
                .get();
    }

    public EstimateDto updateEstimate(EstimateDto estimateDto) throws Exception {
        try {
            Estimate estimate = estimateRepo.findEstimateById(estimateDto.getId()).get();
            Estimate modifiedEstimate = this.saveChanges(estimateDto, estimate);
            estimateRepo.save(modifiedEstimate);
            return estimateRepo.findEstimateById(modifiedEstimate.getId()).stream()
                    .map(source -> modelMapper.map(source, EstimateDto.class))
                    .findFirst()
                    .get();
        } catch (NoSuchElementException e) {
            throw new Exception("Dato non trovato");
        }

    }

    public List<EstimateDto> findAllEstimates() {
        List<Estimate> estimates = estimateRepo.findAll();
        return estimates.stream()
                .map(source -> modelMapper.map(source, EstimateDto.class))
                .toList();
    }

    public Optional<EstimateDto> findEstimateById(Long id) {
        Optional<Estimate> estimate = estimateRepo.findEstimateById(id);
        return estimate.stream()
                .map(source -> modelMapper.map(source, EstimateDto.class))
                .findFirst();
    }

    public List<EstimateDto> findEstimatesByClientId(Long id) throws Exception {
        try {
            List<Estimate> estimates = estimateRepo.findEstimatesByClient(clientRepo.findClientById(id).get());
            return estimates.stream()
                    .map(source -> modelMapper.map(source, EstimateDto.class))
                    .toList();
        } catch (NoSuchElementException e) {
            throw new Exception("Dato non trovato");
        }

    }

    public List<EstimateDto> findEstimateByEmployeeId(Long id) throws Exception {
        try {
            List<Estimate> estimates = estimateRepo.findEstimatesByEmployee(employeeRepo.findEmployeeById(id).get());
            return estimates.stream()
                    .map(source -> modelMapper.map(source, EstimateDto.class))
                    .toList();
        } catch (NoSuchElementException e) {
            throw new Exception("Dato non trovato");
        }

    }

    public void deleteEstimate(Long id) {
        estimateRepo.deleteEstimateById(id);
    }

    private Estimate saveChanges(EstimateDto estimateDto) throws Exception {
        try {
            Estimate estimate = new Estimate();
            estimate.setClient(clientRepo.findClientById(estimateDto.getClient().getId()).get());
            estimate.setEmployee(employeeRepo.findEmployeeByEmail("default").get());
            estimate.setProduct(productRepo.findProductById(estimateDto.getProduct().getId()).get());
            estimate.setOptions(this.modifyOptions(estimateDto.getOptions()));
            estimate.setPrice(estimateDto.getPrice());
            return estimate;
        } catch (NoSuchElementException e) {
            throw new Exception("Qualcuno dei dati non è stato trovato");
        }

    }

    private Estimate saveChanges(EstimateDto estimateDto, Estimate estimate) throws Exception {
        try {
            if (!estimateDto.getPrice().equals(estimate.getPrice())) {
                if (estimateDto.getEmployee().getId().equals(estimate.getEmployee().getId()) || estimate.getEmployee().getName().equals("default")) {
                    estimate.setPrice(estimateDto.getPrice());
                    estimate.setEmployee(employeeRepo.findEmployeeById(estimateDto.getEmployee().getId()).get());
                } else {
                    throw new Exception("Tentativo modifica informazioni di base del preventivo (impiegato)");
                }
            } else if (estimateDto.getProduct().getId().equals(estimate.getProduct().getId()) &&
                    estimateDto.getClient().getId().equals(estimate.getClient().getId()) &&
                    estimateDto.getEmployee().getId().equals(estimate.getEmployee().getId())
            ) {
                estimate.setOptions(this.modifyOptions(estimateDto.getOptions()));
            } else {
                throw new Exception("Tentativo modifica informazioni di base del preventivo (cliente)");
            }
            return estimate;
        } catch (NoSuchElementException e) {
            throw new Exception("Qualcuno dei dati non è stato trovato");
        }

    }

    private Set<Opt> modifyOptions(Set<OptDto> optionDto) throws Exception {
        try {
            Set<Opt> opts = new HashSet<>();
            for (OptDto opt: optionDto) {
                opts.add(optionRepo.findOptById(opt.getId()).get());
            }
            return opts;
        } catch (NoSuchElementException e) {
            throw new Exception("Un'opzione non è stata trovata");
        }
    }
}
