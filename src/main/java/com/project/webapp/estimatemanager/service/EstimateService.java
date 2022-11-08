package com.project.webapp.estimatemanager.service;

import com.project.webapp.estimatemanager.models.Estimate;
import com.project.webapp.estimatemanager.repository.EstimateRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EstimateService {
    private final EstimateRepo estimateRepo;

    @Autowired
    public EstimateService(EstimateRepo estimateRepo) {
        this.estimateRepo = estimateRepo;
    }

    public Estimate addEstimate(Estimate estimate) {
        return estimateRepo.save(estimate);
    }

    public List<Estimate> findAllEstimates() {
        return estimateRepo.findAll();
    }

    public Estimate updateEstimate(Estimate estimate) {
        return estimateRepo.save(estimate);
    }

    public Optional<Estimate> findEstimateById(Long id) {
        return estimateRepo.findEstimateById(id);
    }

    //public Optional<Estimate> findEstimateByClientId(Long client_id) {
        //return estimateRepo.findEstimateByClient(client_id);
    //}

    //public Optional<Estimate> findEstimateByEmployeeId(Long employee_id) {
        //return estimateRepo.findEstimateByEmployee(employee_id);
    //}

    public void deleteEstimate(Long id) {
        estimateRepo.deleteEstimateById(id);
    }
}
